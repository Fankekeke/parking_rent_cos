package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 停车出入库管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ParkOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 停车位ID
     */
    private Integer spaceId;

    /**
     * 车辆ID
     */
    private Integer vehicleId;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 开始停车时间
     */
    private String startDate;

    /**
     * 停车结束时间
     */
    private String endDate;

    /**
     * 停车时常（分钟）
     */
    private BigDecimal totalTime;

    /**
     * 停车位单价
     */
    private BigDecimal price;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 押金
     */
    private BigDecimal rentPrice;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 支付状态（0.未支付 1.已支付 2.押金退款中 3.完成 4.驳回）
     */
    private String status;

    /**
     * 备注
     */
    private String content;

    @TableField(exist = false)
    private String spaceName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String vehicleNumber;

    @TableField(exist = false)
    private Integer userId;
}
