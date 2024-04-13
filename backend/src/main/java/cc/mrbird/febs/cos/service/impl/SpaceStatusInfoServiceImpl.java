package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import cc.mrbird.febs.cos.dao.SpaceStatusInfoMapper;
import cc.mrbird.febs.cos.service.ISpaceStatusInfoService;
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
public class SpaceStatusInfoServiceImpl extends ServiceImpl<SpaceStatusInfoMapper, SpaceStatusInfo> implements ISpaceStatusInfoService {

    /**
     * 分页获取车位状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 车位状态信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceStatusInfo> page, SpaceStatusInfo spaceStatusInfo) {
        return baseMapper.selectSpacePage(page, spaceStatusInfo);
    }

    /**
     * 获取车位状态图
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectStatusCheck() {
        return baseMapper.selectStatusCheck();
    }
}
