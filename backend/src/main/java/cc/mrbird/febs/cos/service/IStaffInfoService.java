package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.StaffInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IStaffInfoService extends IService<StaffInfo> {

    /**
     * 分页获取员工信息信息
     *
     * @param page        分页对象
     * @param staffInfo 员工信息信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStaffPage(Page<StaffInfo> page, StaffInfo staffInfo);

    /**
     * 新增员工信息
     *
     * @param staffInfo 员工信息
     * @return 结果
     */
    boolean saveStaff(StaffInfo staffInfo) throws Exception;

    /**
     * 更新员工状态
     *
     * @param staffId 员工ID
     * @param status  状态
     * @return 结果
     */
    boolean accountStatusEdit(Integer staffId, Integer status) throws Exception;
}
