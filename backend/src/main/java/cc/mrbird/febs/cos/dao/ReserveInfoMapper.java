package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ReserveInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface ReserveInfoMapper extends BaseMapper<ReserveInfo> {

    /**
     * 分页获取车位预约信息
     *
     * @param page        分页对象
     * @param reserveInfo 车位预约信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectReservePage(Page<ReserveInfo> page, @Param("reserveInfo") ReserveInfo reserveInfo);
}
