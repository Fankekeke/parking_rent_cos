package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.RuleInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IRuleInfoService extends IService<RuleInfo> {

    /**
     * 分页获取会员价格信息
     *
     * @param page     分页对象
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRulePage(Page<RuleInfo> page, RuleInfo ruleInfo);
}
