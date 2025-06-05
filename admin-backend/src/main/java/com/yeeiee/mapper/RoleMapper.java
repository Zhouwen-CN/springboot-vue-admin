package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeeiee.domain.entity.Role;
import com.yeeiee.domain.vo.RoleMenuVo;
import com.yeeiee.domain.vo.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface RoleMapper extends BaseMapper<Role> {

    IPage<RoleMenuVo> selectRolePages(Page<RoleMenuVo> page, String searchName);

    List<RoleVo> selectRoleVoList();

    List<String> selectRoleNamesByUserId(@Param("userId") Long id);
}
