package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.VehicleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface VehicleInfoMapper extends BaseMapper<VehicleInfo> {

    /**
     * 分页获取车辆信息信息
     *
     * @param page        分页对象
     * @param vehicleInfo 车辆信息信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectVehiclePage(Page<VehicleInfo> page, @Param("vehicleInfo") VehicleInfo vehicleInfo);
}
