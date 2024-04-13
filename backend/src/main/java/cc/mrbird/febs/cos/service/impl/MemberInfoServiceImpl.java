package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MemberInfo;
import cc.mrbird.febs.cos.dao.MemberInfoMapper;
import cc.mrbird.febs.cos.service.IMemberInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class MemberInfoServiceImpl extends ServiceImpl<MemberInfoMapper, MemberInfo> implements IMemberInfoService {

    /**
     * 分页获取会员信息
     *
     * @param page       分页对象
     * @param memberInfo 会员信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMemberPage(Page<MemberInfo> page, MemberInfo memberInfo) {
        return baseMapper.selectMemberPage(page, memberInfo);
    }
}
