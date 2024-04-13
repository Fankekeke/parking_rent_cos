package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SpaceInfo;
import cc.mrbird.febs.cos.dao.SpaceInfoMapper;
import cc.mrbird.febs.cos.service.ISpaceInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class SpaceInfoServiceImpl extends ServiceImpl<SpaceInfoMapper, SpaceInfo> implements ISpaceInfoService {

    /**
     * 分页获取车位信息
     *
     * @param page      分页对象
     * @param spaceInfo 车位信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceInfo> page, SpaceInfo spaceInfo) {
        return baseMapper.selectSpacePage(page, spaceInfo);
    }

    /**
     * 查询空闲停车位
     *
     * @return 结果
     */
    @Override
    public List<SpaceInfo> selectFreeSpace() {
        return baseMapper.selectFreeSpace();
    }
}
