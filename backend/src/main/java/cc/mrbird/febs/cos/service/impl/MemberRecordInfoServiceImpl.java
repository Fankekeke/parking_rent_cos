package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MemberRecordInfo;
import cc.mrbird.febs.cos.dao.MemberRecordInfoMapper;
import cc.mrbird.febs.cos.service.IMemberRecordInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class MemberRecordInfoServiceImpl extends ServiceImpl<MemberRecordInfoMapper, MemberRecordInfo> implements IMemberRecordInfoService {

    /**
     * 分页获取会员信息
     *
     * @param page         分页对象
     * @param memberRecordInfo 会员信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMemberRecordPage(Page<MemberRecordInfo> page, MemberRecordInfo memberRecordInfo) {
        return baseMapper.selectMemberRecordPage(page, memberRecordInfo);
    }
}
