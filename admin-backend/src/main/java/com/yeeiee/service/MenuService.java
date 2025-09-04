package com.yeeiee.service;

import com.yeeiee.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.form.MenuForm;
import com.yeeiee.domain.vo.MenuVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author chen
 * @since 2025-08-26
 */
public interface MenuService extends IService<Menu> {
    /**
     * 添加菜单
     *
     * @param menuForm 菜单
     */
    void addMenu(MenuForm menuForm);

    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    void removeMenu(Long id);

    /**
     * 根据角色名查询菜单
     *
     * @return 菜单列表
     */
    List<MenuVo> getMenuListByRoleNames(List<String> roles);

    /**
     * 根据角色id，查询菜单id列表
     * @param roleId 角色id
     * @return 菜单id列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);
}
