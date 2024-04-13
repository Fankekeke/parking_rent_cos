package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SpaceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface SpaceInfoMapper extends BaseMapper<SpaceInfo> {

    /**
     * 分页获取车位信息
     *
     * @param page      分页对象
     * @param spaceInfo 车位信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceInfo> page, @Param("spaceInfo") SpaceInfo spaceInfo);

    /**
     * 查询空闲停车位
     *
     * @return 结果
     */
    List<SpaceInfo> selectFreeSpace();
}
