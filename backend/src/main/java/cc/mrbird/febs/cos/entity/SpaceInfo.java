package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 停车位管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SpaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 停车位编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 位置
     */
    private String space;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 位置图片
     */
    private String images;

    /**
     * 价格/时
     */
    private BigDecimal price;


}
