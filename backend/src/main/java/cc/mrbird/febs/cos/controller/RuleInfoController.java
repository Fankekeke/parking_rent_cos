package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RuleInfo;
import cc.mrbird.febs.cos.service.IRuleInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/rule-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleInfoController {

    private final IRuleInfoService ruleInfoService;

    /**
     * 分页获取会员价格信息
     *
     * @param page     分页对象
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<RuleInfo> page, RuleInfo ruleInfo) {
        return R.ok(ruleInfoService.selectRulePage(page, ruleInfo));
    }

    /**
     * 会员价格信息详情
     *
     * @param id 会员价格ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(ruleInfoService.getById(id));
    }

    /**
     * 会员价格信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(ruleInfoService.list());
    }

    /**
     * 新增会员价格信息
     *
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    @PostMapping
    public R save(RuleInfo ruleInfo) {
        ruleInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        ruleInfo.setCode("RU-" + System.currentTimeMillis());
        return R.ok(ruleInfoService.save(ruleInfo));
    }

    /**
     * 修改会员价格信息
     *
     * @param ruleInfo 会员价格信息
     * @return 结果
     */
    @PutMapping
    public R edit(RuleInfo ruleInfo) {
        return R.ok(ruleInfoService.updateById(ruleInfo));
    }

    /**
     * 删除会员价格信息
     *
     * @param ids ids
     * @return 会员价格信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(ruleInfoService.removeByIds(ids));
    }
}
