package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.Menu;
import com.yeeiee.entity.RoleMenu;
import com.yeeiee.entity.vo.MenuVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.mapper.RoleMenuMapper;
import com.yeeiee.service.MenuService;
import com.yeeiee.utils.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@AllArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private MenuMapper menuMapper;
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void addMenu(Menu menu) {
        menuMapper.insert(menu);

        // 每次添加菜单，都会赋予admin菜单权限
        val roleMenu = new RoleMenu();
        roleMenu.setRoleId(1L);
        roleMenu.setMenuId(menu.getId());

        roleMenuMapper.insert(roleMenu);
    }

    @Override
    public void removeMenu(Long id) {
        val parentMenus = menuMapper.selectList(new QueryWrapper<Menu>()
                .lambda()
                .eq(Menu::getPid, id)
        );
        if (!parentMenus.isEmpty()) {
            val parentMenuIds = parentMenus.stream().map(Menu::getId).toList();
            throw new DmlOperationException("删除失败，尚有子菜单依赖：" + parentMenuIds);
        }

        // 1-4 是权限菜单
        if (id <= 4) {
            throw new DmlOperationException("删除失败，权限菜单不能删除");
        }

        menuMapper.deleteById(id);

        val roleMenuList = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>()
                .lambda()
                .eq(RoleMenu::getMenuId, id)
        );
        roleMenuMapper.deleteByIds(roleMenuList);
    }

    @Override
    public List<MenuVo> getMenuList(Collection<Long> ids) {
        val menuList = menuMapper.selectMenusByRoleIds(ids);
        return CollectionUtil.makeTree(menuList,
                MenuVo::getId,
                MenuVo::getPid,
                MenuVo::getChildren,
                MenuVo::setChildren,
                (id) -> id == 0L);
    }
}
