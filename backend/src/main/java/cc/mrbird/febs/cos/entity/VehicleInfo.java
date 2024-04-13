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
 * 车辆管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VehicleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 车辆编号
     */
    private String vehicleNo;

    /**
     * 车牌号
     */
    private String vehicleNumber;

    /**
     * 车辆颜色
     */
    private String vehicleColor;

    /**
     * 车辆名称
     */
    private String name;

    /**
     * 发动机号码
     */
    private String engineNo;

    /**
     * 排放标准
     */
    private String emissionStandard;

    /**
     * 燃料类型（1.燃油 2.柴油 3.油电混动 4.电能）
     */
    private String fuelType;

    /**
     * 照片
     */
    private String images;

    /**
     * 备注
     */
    private String content;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 所属用户
     */
    private Integer userId;

    @TableField(exist = false)
    private String userName;
}
