package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/park-order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkOrderInfoController {

    private final IParkOrderInfoService parkOrderInfoService;

    private final IUserInfoService userInfoService;

    private final IMemberInfoService memberInfoService;

    private final ISpaceInfoService spaceInfoService;

    private final IReserveInfoService reserveInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    private final IMessageInfoService messageInfoService;

    private final IVehicleInfoService vehicleInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ParkOrderInfo> page, ParkOrderInfo parkOrderInfo) {
        return R.ok(parkOrderInfoService.selectOrderPage(page, parkOrderInfo));
    }

    /**
     * 订单押金退款
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PutMapping("/orderRefund")
    @Transactional(rollbackFor = Exception.class)
    public R orderRefund(ParkOrderInfo parkOrderInfo) {

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(parkOrderInfo.getVehicleId());

        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));

        // 发送消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userInfo.getId());

        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));

        Context context = new Context();
        context.setVariable("today", DateUtil.formatDate(new Date()));

        if ("3".equals(parkOrderInfo.getStatus())) {
            messageInfo.setContent("您好，您的订单 " + parkOrderInfo.getCode() + "押金已退回，请注意查收");
            context.setVariable("custom", "您好，您的订单 " + parkOrderInfo.getCode() + "押金已退回，请注意查收");
        }
        if ("4".equals(parkOrderInfo.getStatus())) {
            messageInfo.setContent("您好，您的订单 " + parkOrderInfo.getCode() + "押金未能成功退回，请查看备注或联系管理员");
            context.setVariable("custom", "您好，您的订单 " + parkOrderInfo.getCode() + "押金未能成功退回，请查看备注或联系管理员");
        }
        messageInfoService.save(messageInfo);
        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "车位订单提示", emailContent);
        }

        // 车位状态变更
        SpaceStatusInfo spaceStatusInfo = spaceStatusInfoService.getOne(Wrappers.<SpaceStatusInfo>lambdaQuery().eq(SpaceStatusInfo::getSpaceId, parkOrderInfo.getSpaceId()));
        spaceStatusInfo.setStatus("0");
        spaceStatusInfoService.updateById(spaceStatusInfo);
        return R.ok(parkOrderInfoService.updateById(parkOrderInfo));
    }

    /**
     * 订单驶出结算
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/order/over")
    @Transactional(rollbackFor = Exception.class)
    public R orderOver(String orderCode) {
        // 获取订单信息
        ParkOrderInfo orderInfo = parkOrderInfoService.getOne(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getCode, orderCode));

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(orderInfo.getVehicleId());

        // 获取用户会员信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        boolean isMember = false;
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    isMember = true;
                }
            }
        }

        orderInfo.setEndDate(DateUtil.formatDateTime(new Date()));
        // 停车总时常
        long totalTime = DateUtil.between(DateUtil.parseDateTime(orderInfo.getStartDate()), DateUtil.parseDateTime(orderInfo.getEndDate()), DateUnit.MINUTE);
        orderInfo.setTotalTime(BigDecimal.valueOf(totalTime));
        // 总价格
        if (isMember) {
            orderInfo.setTotalPrice(BigDecimal.ZERO);
            orderInfo.setStatus("1");
        } else {
            BigDecimal unit = NumberUtil.div(orderInfo.getTotalTime(), new BigDecimal(60), 2);
            orderInfo.setTotalPrice(NumberUtil.mul(unit, orderInfo.getPrice()));
        }

        // 发送消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userInfo.getId());
        messageInfo.setContent("您好，您的车辆 " + vehicleInfo.getVehicleNumber() + "已驶出，请前往订单缴费");
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        messageInfoService.save(messageInfo);
        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + " 您好，您的车辆 " + vehicleInfo.getVehicleNumber() + "已驶出，请前往订单缴费");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "车位订单提示", emailContent);
        }

        // 车位状态变更
        SpaceStatusInfo spaceStatusInfo = spaceStatusInfoService.getOne(Wrappers.<SpaceStatusInfo>lambdaQuery().eq(SpaceStatusInfo::getSpaceId, orderInfo.getSpaceId()));
        spaceStatusInfo.setStatus("0");
        spaceStatusInfoService.updateById(spaceStatusInfo);
        return R.ok(parkOrderInfoService.updateById(orderInfo));
    }

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    @GetMapping("/home/data")
    public R homeData() {
        return R.ok(parkOrderInfoService.homeData());
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    @GetMapping("/statistics")
    public R selectRoomStatistics(@RequestParam(value = "checkDate", required = false) String checkDate) {
        return R.ok(parkOrderInfoService.selectStatistics(checkDate));
    }

    /**
     * 订单信息详情
     *
     * @param id 订单ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(parkOrderInfoService.getById(id));
    }

    /**
     * 订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(parkOrderInfoService.list());
    }

    /**
     * 新增订单信息
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(ParkOrderInfo parkOrderInfo) throws FebsException {
        // 校验车辆是否可以添加订单
        List<ParkOrderInfo> orderInfoList = parkOrderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getVehicleId, parkOrderInfo.getVehicleId()).eq(ParkOrderInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(orderInfoList)) {
            throw new FebsException("此车辆正在停车中或有未缴费订单");
        }

        // 是否正在预约中
        List<ReserveInfo> reserveInfoList = reserveInfoService.list(Wrappers.<ReserveInfo>lambdaQuery().eq(ReserveInfo::getVehicleId, parkOrderInfo.getVehicleId()).eq(ReserveInfo::getStatus, "1"));
        if (CollectionUtil.isNotEmpty(reserveInfoList)) {
            List<Integer> ids = reserveInfoList.stream().map(ReserveInfo::getId).collect(Collectors.toList());

            List<Integer> spaceIds = reserveInfoList.stream().map(ReserveInfo::getSpaceId).collect(Collectors.toList());
            reserveInfoService.update(Wrappers.<ReserveInfo>lambdaUpdate().set(ReserveInfo::getStatus, "0").in(ReserveInfo::getId, ids));
            // 更新预约车位状态
            spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, "0").in(SpaceStatusInfo::getSpaceId, spaceIds));
        }

        // 车位信息
        SpaceInfo spaceInfo = spaceInfoService.getById(parkOrderInfo.getSpaceId());

        // 更新车位状态
        spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, "1").eq(SpaceStatusInfo::getSpaceId, spaceInfo.getId()));

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(parkOrderInfo.getVehicleId());
        // 用户信息
        UserInfo userInfo = userInfoService.getById(vehicleInfo.getUserId());

        // 发送消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userInfo.getId());
        messageInfo.setContent("您好，您的车辆 " + vehicleInfo.getVehicleNumber() + "订单已生成，请注意查看");
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        messageInfoService.save(messageInfo);
        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + " 您好。您的车辆 "+vehicleInfo.getVehicleNumber()+" 订单已生成，请注意查看");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "车位订单提示", emailContent);
        }

        // 订单信息
        parkOrderInfo.setCode("ORD-" + System.currentTimeMillis());
        parkOrderInfo.setPrice(spaceInfo.getPrice());
        parkOrderInfo.setStatus("0");
        parkOrderInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        return R.ok(parkOrderInfoService.save(parkOrderInfo));
    }

    /**
     * 添加车位订单
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/orderAdd")
    @Transactional(rollbackFor = Exception.class)
    public R orderAdd(ParkOrderInfo parkOrderInfo) throws FebsException {
        // 判断此车辆是否可预约
        List<ParkOrderInfo> orderInfoList = parkOrderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getVehicleId, parkOrderInfo.getVehicleId()).eq(ParkOrderInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(orderInfoList)) {
            throw new FebsException("此车辆正在停车中或有未缴费订单");
        }

        // 如果维修结束时间大于当前时间（维修状态已完成）
        if (DateUtil.compare(DateUtil.parseDate(parkOrderInfo.getEndDate()), new DateTime()) >= 0) {
            throw new FebsException("租赁结束时间不能小于当前时间");
        }

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(parkOrderInfo.getVehicleId());

        // 车位信息
        SpaceInfo spaceInfo = spaceInfoService.getById(parkOrderInfo.getSpaceId());

        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));

        // 发送消息
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(userInfo.getId());
        messageInfo.setContent("您好，您的"+vehicleInfo.getVehicleNumber()+"车位已添加成功，请前往我的订单支付");
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        messageInfoService.save(messageInfo);
        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", "您好，您的"+vehicleInfo.getVehicleNumber()+"车位已添加成功，请前往我的订单支付");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "预定提示", emailContent);
        }

        // 订单信息
        parkOrderInfo.setCode("ORD-" + System.currentTimeMillis());
        parkOrderInfo.setPrice(spaceInfo.getPrice());
        parkOrderInfo.setStatus("0");
        parkOrderInfo.setStartDate(DateUtil.formatDateTime(new Date()));

        // 停车总时常
        long totalTime = DateUtil.between(DateUtil.parseDateTime(parkOrderInfo.getStartDate()), DateUtil.parseDateTime(parkOrderInfo.getEndDate()), DateUnit.MINUTE);
        parkOrderInfo.setTotalTime(BigDecimal.valueOf(totalTime));
        BigDecimal unit = NumberUtil.div(parkOrderInfo.getTotalTime(), new BigDecimal(60), 2);
        parkOrderInfo.setTotalPrice(NumberUtil.add(new BigDecimal("300"), NumberUtil.mul(unit, parkOrderInfo.getPrice())));
        parkOrderInfo.setRentPrice(new BigDecimal("300"));

        return R.ok(parkOrderInfoService.save(parkOrderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(ParkOrderInfo parkOrderInfo) {
        return R.ok(parkOrderInfoService.updateById(parkOrderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(parkOrderInfoService.removeByIds(ids));
    }
}
