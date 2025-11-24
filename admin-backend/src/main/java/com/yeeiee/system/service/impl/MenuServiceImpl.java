package com.yeeiee.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.common.exception.DmlOperationException;
import com.yeeiee.common.utils.BeanUtil;
import com.yeeiee.common.utils.CollectionUtil;
import com.yeeiee.system.domain.entity.Menu;
import com.yeeiee.system.domain.entity.RoleMenu;
import com.yeeiee.system.domain.form.MenuForm;
import com.yeeiee.system.domain.vo.MenuVo;
import com.yeeiee.system.mapper.MenuMapper;
import com.yeeiee.system.service.MenuService;
import com.yeeiee.system.service.RoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-08-26
 */
@RequiredArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    private final RoleMenuService roleMenuService;
    private final MenuMapper menuMapper;

    @Override
    public void addMenu(MenuForm menuForm) {
        val exists = this.exists(
                Wrappers.<Menu>lambdaQuery()
                        .eq(Menu::getTitle, menuForm.getTitle())
                        .or()
                        .eq(Menu::getAccessPath, menuForm.getAccessPath())
        );

        if (exists) {
            throw new DmlOperationException("菜单标题 或 访问路径已经存在");
        }

        val menu = BeanUtil.toBean(menuForm, Menu.class);
        this.save(menu);

        // TODO: 每次添加菜单，都会赋予admin菜单权限
        val roleMenu = new RoleMenu();
        roleMenu.setRoleId(1L);
        roleMenu.setMenuId(menu.getId());

        roleMenuService.save(roleMenu);
    }

    @Override
    public void removeMenu(Long id) {
        val parentMenus = this.lambdaQuery()
                .eq(Menu::getPid, id)
                .list();

        if (!parentMenus.isEmpty()) {
            val parentMenuIds = parentMenus.stream().map(Menu::getId).toList();
            throw new DmlOperationException("删除失败，尚有子菜单依赖：" + parentMenuIds);
        }

        // TODO: 演示项目禁止删除功能菜单
        if (id <= 18L) {
            throw new DmlOperationException("删除失败，演示项目不能删除功能菜单");
        }

        this.removeById(id);

        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery()
                .eq(RoleMenu::getMenuId, id)
        );
    }

    @Override
    public List<MenuVo> getMenuListByRoleNames(List<String> roles) {
        val menuList = menuMapper.selectMenusByRoleNames(roles);
        return CollectionUtil.makeTree(menuList,
                MenuVo::getId,
                MenuVo::getPid,
                MenuVo::getChildren,
                MenuVo::setChildren,
                (id) -> id == 0L);
    }
}
