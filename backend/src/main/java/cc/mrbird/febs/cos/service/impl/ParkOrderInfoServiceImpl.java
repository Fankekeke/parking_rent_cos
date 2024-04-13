package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.BulletinInfo;
import cc.mrbird.febs.cos.entity.ParkOrderInfo;
import cc.mrbird.febs.cos.dao.ParkOrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkOrderInfoServiceImpl extends ServiceImpl<ParkOrderInfoMapper, ParkOrderInfo> implements IParkOrderInfoService {

    private final IUserInfoService userInfoService;
    private final IVehicleInfoService vehicleInfoService;
    private final ISpaceInfoService spaceInfoService;
    private final IBulletinInfoService bulletinInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<ParkOrderInfo> page, ParkOrderInfo parkOrderInfo) {
        return baseMapper.selectOrderPage(page, parkOrderInfo);
    }

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        // 管理员展示信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 本月收益
        BigDecimal incomeMonth = baseMapper.selectIncomeMonth();
        // 本月工单
        Integer workOrderMonth = baseMapper.selectWorkOrderMonth();
        // 本年收益
        BigDecimal incomeYear = baseMapper.selectIncomeYear();
        // 本年工单
        Integer workOrderYear = baseMapper.selectWorkOrderYear();
        // 客户数量
        Integer userNum = userInfoService.count();
        // 车辆数量
        Integer staffNum = vehicleInfoService.count();
        // 车位数量
        Integer roomNum = spaceInfoService.count();
        // 总收益
        BigDecimal amount = baseMapper.selectAmountPrice();
        // 十天内缴费记录
        List<LinkedHashMap<String, Object>> paymentRecord = baseMapper.selectPaymentRecord();
        // 十天内工单数量
        List<LinkedHashMap<String, Object>> orderRecord = baseMapper.selectOrderRecord();
        result.put("incomeMonth", incomeMonth);
        result.put("workOrderMonth", workOrderMonth);
        result.put("incomeYear", incomeYear);
        result.put("workOrderYear", workOrderYear);
        result.put("totalOrderNum", userNum);
        result.put("staffNum", staffNum);
        result.put("roomNum", roomNum);
        result.put("totalRevenue", amount);
        result.put("paymentRecord", paymentRecord);
        result.put("orderRecord", orderRecord);

        // 公告信息
        List<BulletinInfo> bulletinList = bulletinInfoService.list();
        result.put("bulletin", bulletinList);
        return result;
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStatistics(String checkDate) {
        if (StrUtil.isEmpty(checkDate)) {
            checkDate = DateUtil.formatDate(new Date());
        }

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 获取当前月份及当前月份
        String year = StrUtil.toString(DateUtil.year(DateUtil.parseDate(checkDate)));
        String month = StrUtil.toString(DateUtil.month(DateUtil.parseDate(checkDate)) + 1);

        // 本月收益
        List<LinkedHashMap<String, Object>> priceByMonth = baseMapper.selectPriceByMonth(year, month, checkDate);
        result.put("priceByMonth", priceByMonth);

        // 本月订单
        List<LinkedHashMap<String, Object>> orderNumByMonth = baseMapper.selectOrderNumByMonth(year, month, checkDate);
        result.put("orderNumByMonth", orderNumByMonth);

        // 类型销量统计
        List<LinkedHashMap<String, Object>> typeOrderNumRateByMonth = baseMapper.selectTypeRateByMonth(year, month);
        result.put("typeOrderNumRateByMonth", typeOrderNumRateByMonth);

        // 类型销售统计
        List<LinkedHashMap<String, Object>> typePriceRateByMonth = baseMapper.selectTypePriceRateByMonth(year, month);
        result.put("typePriceRateByMonth", typePriceRateByMonth);

        return result;
    }

    /**
     * 添加车位订单
     *
     * @param parkingOrderInfo 订单信息
     * @return 结果
     */
    @Override
    public boolean orderAdd(ParkOrderInfo parkingOrderInfo) {

        return false;
    }
}
