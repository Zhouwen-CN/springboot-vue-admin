package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.Menu;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface MenuService extends IService<Menu> {

    /**
     * 添加菜单
     *
     * @param menu 菜单
     */
    void addMenu(Menu menu);

    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    void deleteMenu(Long id);

    /**
     * 根据角色id查询菜单
     *
     * @return 菜单列表
     */
    List<Menu> getMenuList(Collection<Long> ids);
}
