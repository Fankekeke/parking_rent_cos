package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RuleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface RuleInfoMapper extends BaseMapper<RuleInfo> {

    /**
     * 分页获取会员价格信息
     *
     * @param page     分页对象
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRulePage(Page<RuleInfo> page, @Param("ruleInfo") RuleInfo ruleInfo);
}
