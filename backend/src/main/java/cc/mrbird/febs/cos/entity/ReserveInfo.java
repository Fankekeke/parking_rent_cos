package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 车位预约信息
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReserveInfo implements Serializable {

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
     * 开始预约时间
     */
    private String startDate;

    /**
     * 预约结束时间
     */
    private String endDate;

    /**
     * 状态（0.结束 1.预约中）
     */
    private String status;

    @TableField(exist = false)
    private String spaceName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String vehicleNumber;

    @TableField(exist = false)
    private Integer userId;
}
