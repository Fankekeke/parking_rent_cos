package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface ISpaceStatusInfoService extends IService<SpaceStatusInfo> {

    /**
     * 分页获取车位状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 车位状态信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceStatusInfo> page, SpaceStatusInfo spaceStatusInfo);

    /**
     * 获取车位状态图
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectStatusCheck();
}
