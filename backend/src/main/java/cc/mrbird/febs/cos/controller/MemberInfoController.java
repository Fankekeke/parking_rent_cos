package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.BulletinInfo;
import cc.mrbird.febs.cos.entity.MemberInfo;
import cc.mrbird.febs.cos.entity.RuleInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.cos.service.IMemberInfoService;
import cc.mrbird.febs.cos.service.IRuleInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/member-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberInfoController {

    private final IMemberInfoService memberInfoService;

    private final IUserInfoService userInfoService;

    private final IRuleInfoService ruleInfoService;

    private final IBulletinInfoService bulletinInfoService;

    /**
     * 分页获取会员信息
     *
     * @param page       分页对象
     * @param memberInfo 会员信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MemberInfo> page, MemberInfo memberInfo) {
        return R.ok(memberInfoService.selectMemberPage(page, memberInfo));
    }

    /**
     * 根据用户ID获取会员信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/member/{userId}")
    public R selectMemberByUserId(@PathVariable("userId") Integer userId) {
        // 返回信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        result.put("user", userInfo);

        // 公告信息
        List<BulletinInfo> bulletinInfoList = bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getType, 1));
        result.put("bulletin", bulletinInfoList);
        // 会员信息
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    RuleInfo ruleInfo = ruleInfoService.getById(memberInfo.getMemberLevel());
                    memberInfo.setRuleName(ruleInfo.getName());
                    result.put("member", memberInfo);
                    return R.ok(result);
                }
            }
        } else {
            result.put("member", null);
        }
        return R.ok(result);
    }

    /**
     * 查询用户是否会员
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/over/{userId}")
    public R selectUserMemberOver(@PathVariable("userId") Integer userId) {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        // 会员信息
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    return R.ok(true);
                }
            }
        } else {
            return R.ok(false);
        }
        return R.ok(false);
    }

    /**
     * 会员信息详情
     *
     * @param id 会员ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(memberInfoService.getById(id));
    }

    /**
     * 会员信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(memberInfoService.list());
    }

    /**
     * 新增会员信息
     *
     * @param memberInfo 会员信息
     * @return 结果
     */
    @PostMapping
    public R save(MemberInfo memberInfo) {
        return R.ok(memberInfoService.save(memberInfo));
    }

    /**
     * 修改会员信息
     *
     * @param memberInfo 会员信息
     * @return 结果
     */
    @PutMapping
    public R edit(MemberInfo memberInfo) {
        return R.ok(memberInfoService.updateById(memberInfo));
    }

    /**
     * 删除会员信息
     *
     * @param ids ids
     * @return 会员信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(memberInfoService.removeByIds(ids));
    }
}
