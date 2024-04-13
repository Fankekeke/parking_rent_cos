package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/reserve-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReserveInfoController {

    private final IReserveInfoService reserveInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IParkOrderInfoService parkOrderInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    private final IMessageInfoService messageInfoService;

    private final IVehicleInfoService vehicleInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取车位预约信息
     *
     * @param page        分页对象
     * @param reserveInfo 车位预约信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ReserveInfo> page, ReserveInfo reserveInfo) {
        return R.ok(reserveInfoService.selectReservePage(page, reserveInfo));
    }

    /**
     * 车位预约信息详情
     *
     * @param id 车位预约ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(reserveInfoService.getById(id));
    }

    /**
     * 车位预约信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(reserveInfoService.list());
    }

    /**
     * 新增车位预约信息
     *
     * @param reserveInfo 车位预约信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(ReserveInfo reserveInfo) throws FebsException {
        // 判断此车辆是否可预约
        List<ParkOrderInfo> orderInfoList = parkOrderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getVehicleId, reserveInfo.getVehicleId()).eq(ParkOrderInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(orderInfoList)) {
            throw new FebsException("此车辆正在停车中或有未缴费订单");
        }
        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(reserveInfo.getVehicleId());

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

        return R.ok(reserveInfoService.save(reserveInfo));
    }

    /**
     * 修改车位预约信息
     *
     * @param reserveInfo 车位预约信息
     * @return 结果
     */
    @PutMapping
    public R edit(ReserveInfo reserveInfo) {
        return R.ok(reserveInfoService.updateById(reserveInfo));
    }

    /**
     * 删除车位预约信息
     *
     * @param ids ids
     * @return 车位预约信息
     */
    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        // 获取预约信息
        List<ReserveInfo> reserveInfoList = (List<ReserveInfo>) reserveInfoService.listByIds(ids);
        for (ReserveInfo reserveInfo : reserveInfoList) {
            if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(reserveInfo.getStartDate()), DateUtil.parseDateTime(reserveInfo.getEndDate()))) {
                spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, 0).eq(SpaceStatusInfo::getId, reserveInfo.getSpaceId()));
            }
        }
        return R.ok(reserveInfoService.removeByIds(ids));
    }
}
