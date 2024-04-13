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
 * 会员购买记录
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MemberRecordInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 状态（0.未支付 1.已支付）
     */
    private String status;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 用户ID
     */
    private Integer userId;

    @TableField(exist = false)
    private String userCode;

    @TableField(exist = false)
    private String name;
}
