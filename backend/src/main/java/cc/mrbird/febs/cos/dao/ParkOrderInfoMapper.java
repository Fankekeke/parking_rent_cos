package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ParkOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface ParkOrderInfoMapper extends BaseMapper<ParkOrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<ParkOrderInfo> page, @Param("parkOrderInfo") ParkOrderInfo parkOrderInfo);

    /**
     * 本月收益
     *
     * @return 结果
     */
    BigDecimal selectIncomeMonth();

    /**
     * 本月工单
     *
     * @return 结果
     */
    Integer selectWorkOrderMonth();

    /**
     * 本年收益
     *
     * @return 结果
     */
    BigDecimal selectIncomeYear();

    /**
     * 本年工单
     *
     * @return 结果
     */
    Integer selectWorkOrderYear();

    /**
     * 获取缴费总收益
     *
     * @return 结果
     */
    BigDecimal selectAmountPrice();

    /**
     * 查询十天内缴费统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPaymentRecord();

    /**
     * 查询十天内工单数量
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderRecord();

    /**
     * 按月统计收益统计
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPriceByMonth(@Param("year") String year, @Param("month") String month, @Param("checkDate") String date);

    /**
     * 按月统计工单统计
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderNumByMonth(@Param("year") String year, @Param("month") String month, @Param("checkDate") String date);

    /**
     * 车位销量比率统计
     *
     * @param year  年费
     * @param month 月份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectTypeRateByMonth(@Param("year") String year, @Param("month") String month);

    /**
     * 车位销售比率统计
     *
     * @param year  年费
     * @param month 月份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectTypePriceRateByMonth(@Param("year") String year, @Param("month") String month);

    /**
     * 根据用户ID获取订单
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<ParkOrderInfo> selectOrderByUserId(@Param("userId") Integer userId);
}
