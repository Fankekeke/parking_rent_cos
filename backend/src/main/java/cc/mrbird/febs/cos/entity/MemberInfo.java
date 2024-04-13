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
 * 会员管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 会员等级（1.月会员 2.年会员）
     */
    private String memberLevel;

    /**
     * 会员开始时间
     */
    private String startDate;

    /**
     * 会员结束时间
     */
    private String endDate;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 支付时间
     */
    private String payDate;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String ruleName;

}
