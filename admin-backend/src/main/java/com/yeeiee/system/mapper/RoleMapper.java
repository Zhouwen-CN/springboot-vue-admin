package com.yeeiee.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.system.domain.entity.Role;
import com.yeeiee.system.domain.vo.RoleSelectorVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-09-05
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<RoleSelectorVo> selectRoleSelectorVoListByUserId(@Param("userId") Long userId);
}
