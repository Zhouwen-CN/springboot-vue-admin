package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.Menu;
import com.yeeiee.entity.RoleMenu;
import com.yeeiee.entity.vo.MenuVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.MenuMapper;
import com.yeeiee.service.MenuService;
import com.yeeiee.service.RoleMenuService;
import com.yeeiee.utils.CollectionUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    private final RoleMenuService roleMenuService;
    private final MenuMapper menuMapper;

    @Override
    public void addMenu(Menu menu) {
        this.save(menu);

        // 每次添加菜单，都会赋予admin菜单权限
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

        // todo: 演示项目禁止删除功能菜单
        if (id <= 13) {
            throw new DmlOperationException("删除失败，演示项目不能删除功能菜单");
        }

        this.removeById(id);

        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, id)
        );
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
