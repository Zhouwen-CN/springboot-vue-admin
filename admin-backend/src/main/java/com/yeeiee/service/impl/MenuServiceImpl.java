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
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
        return convertToMenuTree(menuList);
    }

    /**
     * 将menus列表转换成树形结构，递归sql性能不太好，使用代码处理
     */
    private List<MenuVo> convertToMenuTree(List<MenuVo> menuList) {
        val treeMap = new HashMap<Long, MenuVo>(8);
        for (MenuVo menu : menuList) {
            val id = menu.getId();
            val pid = menu.getPid();

            // 如果节点不存在，则添加当前节点
            if (!treeMap.containsKey(id)) {
                treeMap.put(id, menu);
            }

            // 合并节点
            val item = treeMap.get(id);
            MenuVo.mergeMenu(item, menu);

            // 父节点不存在，创建空节点
            if (!treeMap.containsKey(pid)) {
                treeMap.put(pid, new MenuVo());
            }
            treeMap.get(pid).getChildren().add(item);
        }

        // 如果用户没有角色，root则为空
        val root = treeMap.get(0L);
        if (root == null) {
            return new ArrayList<>();
        }

        return root.getChildren();
    }
}
