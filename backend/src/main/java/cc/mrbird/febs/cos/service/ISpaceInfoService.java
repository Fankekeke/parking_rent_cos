package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SpaceInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface ISpaceInfoService extends IService<SpaceInfo> {

    /**
     * 分页获取车位信息
     *
     * @param page      分页对象
     * @param spaceInfo 车位信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceInfo> page, SpaceInfo spaceInfo);

    /**
     * 查询空闲停车位
     *
     * @return 结果
     */
    List<SpaceInfo> selectFreeSpace();
}
