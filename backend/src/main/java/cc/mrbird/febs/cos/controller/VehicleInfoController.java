package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.entity.VehicleInfo;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cc.mrbird.febs.cos.service.IVehicleInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/cos/vehicle-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleInfoController {

    private final IVehicleInfoService vehicleInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取车辆信息信息
     *
     * @param page        分页对象
     * @param vehicleInfo 车辆信息信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<VehicleInfo> page, VehicleInfo vehicleInfo) {
        return R.ok(vehicleInfoService.selectVehiclePage(page, vehicleInfo));
    }

    /**
     * 车辆信息信息详情
     *
     * @param id 车辆信息ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(vehicleInfoService.getById(id));
    }

    /**
     * 根据用户ID获取车辆信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/user/{userId}")
    public R selectVehicleByUserId(@PathVariable("userId") Integer userId) {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        return R.ok(vehicleInfoService.list(Wrappers.<VehicleInfo>lambdaQuery().eq(VehicleInfo::getUserId, userInfo.getId())));
    }

    /**
     * 车辆信息信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(vehicleInfoService.list());
    }

    /**
     * 新增车辆信息信息
     *
     * @param vehicleInfo 车辆信息信息
     * @return 结果
     */
    @PostMapping
    public R save(VehicleInfo vehicleInfo) {
        vehicleInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        vehicleInfo.setVehicleNo("AD-" + System.currentTimeMillis());
        return R.ok(vehicleInfoService.save(vehicleInfo));
    }

    /**
     * 修改车辆信息信息
     *
     * @param vehicleInfo 车辆信息信息
     * @return 结果
     */
    @PutMapping
    public R edit(VehicleInfo vehicleInfo) {
        return R.ok(vehicleInfoService.updateById(vehicleInfo));
    }

    /**
     * 删除车辆信息信息
     *
     * @param ids ids
     * @return 车辆信息信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(vehicleInfoService.removeByIds(ids));
    }
}
