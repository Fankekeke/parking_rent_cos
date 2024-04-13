package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ReserveInfo;
import cc.mrbird.febs.cos.dao.ReserveInfoMapper;
import cc.mrbird.febs.cos.service.IReserveInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class ReserveInfoServiceImpl extends ServiceImpl<ReserveInfoMapper, ReserveInfo> implements IReserveInfoService {

    /**
     * 分页获取车位预约信息
     *
     * @param page        分页对象
     * @param reserveInfo 车位预约信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectReservePage(Page<ReserveInfo> page, ReserveInfo reserveInfo) {
        return baseMapper.selectReservePage(page, reserveInfo);
    }
}
