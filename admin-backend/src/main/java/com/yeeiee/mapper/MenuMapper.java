package com.yeeiee.mapper;

import com.yeeiee.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.domain.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-08-26
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据角色列表查询拥有权限的菜单，一个用户可能有多个角色
     *
     * @param roles 角色名列表
     * @return 菜单列表
     */
    List<MenuVo> selectMenusByRoleNames(@Param("roles") List<String> roles);
}
