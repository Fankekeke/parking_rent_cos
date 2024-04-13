package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.service.CacheService;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.dao.StaffInfoMapper;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffInfoServiceImpl extends ServiceImpl<StaffInfoMapper, StaffInfo> implements IStaffInfoService {

    private final UserService userService;

    private final CacheService cacheService;

    /**
     * 分页获取员工信息信息
     *
     * @param page        分页对象
     * @param staffInfo 员工信息信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStaffPage(Page<StaffInfo> page, StaffInfo staffInfo) {
        return baseMapper.selectStaffPage(page, staffInfo);
    }

    /**
     * 新增员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStaff(StaffInfo staffInfo) throws Exception {
        // 设置员工编号
        staffInfo.setCode("STF-" + System.currentTimeMillis());
        staffInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        staffInfo.setStatus(1);
        User user = new User();
        user.setUsername(staffInfo.getCode());
        user.setStatus("1");
        user.setRoleId("76");
        // 添加维修员账户
        userService.createUser(user);
        staffInfo.setUserId(user.getUserId());
        return this.save(staffInfo);
    }

    /**
     * 更新员工状态
     *
     * @param staffId 员工ID
     * @param status  状态
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean accountStatusEdit(Integer staffId, Integer status) throws Exception {
        // 获取员工信息
        StaffInfo staffInfo = this.getById(staffId);
        // 获取账户信息
        User user = userService.getById(staffInfo.getUserId());
        // 设置账户状态
        String accountStatus = status == 1 ? "1" : "0";
        user.setStatus(accountStatus);
        staffInfo.setStatus(status);
        userService.updateById(user);
        // 重新将用户信息，用户角色信息，用户权限信息 加载到 redis中
        cacheService.saveUser(user.getUsername());
        cacheService.saveRoles(user.getUsername());
        cacheService.savePermissions(user.getUsername());
        return this.updateById(staffInfo);
    }
}
