package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.RuleInfo;
import cc.mrbird.febs.cos.dao.RuleInfoMapper;
import cc.mrbird.febs.cos.service.IRuleInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class RuleInfoServiceImpl extends ServiceImpl<RuleInfoMapper, RuleInfo> implements IRuleInfoService {

    /**
     * 分页获取会员价格信息
     *
     * @param page     分页对象
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRulePage(Page<RuleInfo> page, RuleInfo ruleInfo) {
        return baseMapper.selectRulePage(page, ruleInfo);
    }
}
