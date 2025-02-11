package com.yeeiee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeeiee.entity.Menu;
import com.yeeiee.entity.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2025-02-11
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据角色列表查询拥有权限的菜单，一个用户可能有多个角色
     *
     * @param roleIds 角色id列表
     * @return 菜单列表
     */
    List<MenuVo> selectMenusByRoleIds(@Param("roleIds") Collection<Long> roleIds);

}
