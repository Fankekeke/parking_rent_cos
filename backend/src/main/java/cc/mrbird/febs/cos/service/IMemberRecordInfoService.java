package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MemberRecordInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IMemberRecordInfoService extends IService<MemberRecordInfo> {

    /**
     * 分页获取会员信息
     *
     * @param page         分页对象
     * @param memberRecordInfo 会员信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMemberRecordPage(Page<MemberRecordInfo> page, MemberRecordInfo memberRecordInfo);
}
