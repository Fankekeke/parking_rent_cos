package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MemberInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface MemberInfoMapper extends BaseMapper<MemberInfo> {

    /**
     * 分页获取会员信息
     *
     * @param page       分页对象
     * @param memberInfo 会员信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMemberPage(Page<MemberInfo> page, @Param("memberInfo") MemberInfo memberInfo);
}
