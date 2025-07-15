package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.domain.entity.Role;
import com.yeeiee.domain.entity.RoleMenu;
import com.yeeiee.domain.entity.UserRole;
import com.yeeiee.domain.form.RoleForm;
import com.yeeiee.domain.vo.RoleMenuVo;
import com.yeeiee.domain.vo.RoleVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.RoleMapper;
import com.yeeiee.service.RoleMenuService;
import com.yeeiee.service.RoleService;
import com.yeeiee.service.UserRoleService;
import com.yeeiee.utils.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@RequiredArgsConstructor
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final UserRoleService userRoleService;
    private final RoleMenuService roleMenuService;
    private final RoleMapper roleMapper;

    @Override
    public IPage<RoleMenuVo> getRolePages(Page<RoleMenuVo> page, String searchName) {
        return roleMapper.selectRolePages(page, searchName);
    }

    @Override
    public void addRole(RoleForm roleForm) {
        val exists = this.exists(
                Wrappers.<Role>lambdaQuery()
                        .eq(Role::getRoleName, roleForm.getRoleName())
        );

        if (exists) {
            throw new DmlOperationException("角色名已经存在");
        }

        val role = new Role();
        role.setRoleName(roleForm.getRoleName());
        role.setDesc(roleForm.getDesc());
        this.save(role);

        val roleMenuList = roleForm.getMenuIds().stream().map(id -> {
            val roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(id);
            return roleMenu;
        }).toList();

        roleMenuService.saveBatch(roleMenuList);
    }

    @Override
    public void modifyRole(RoleForm roleForm) {
        val roleId = roleForm.getId();

        // todo: ① 号角色不能修改
        if (roleId == 1L) {
            throw new DmlOperationException("① 号角色不能修改");
        }

        // 修改角色
        val role = new Role();
        role.setId(roleId);
        role.setRoleName(roleForm.getRoleName());
        role.setDesc(roleForm.getDesc());
        this.updateById(role);

        // 获取 role menu 关系
        val roleMenus = roleMenuService.lambdaQuery()
                .eq(RoleMenu::getRoleId, roleId)
                .list();

        // 获取当前 ids 和 更新的 ids 做差集
        val currentMenuIds = roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .toList();
        val updateMenuIds = roleForm.getMenuIds();

        val pair = CollectionUtil.differenceSet(currentMenuIds, updateMenuIds);

        // 当前 - 更新 = 删除
        val deleteSet = pair.getLeft();
        if (!deleteSet.isEmpty()) {
            val deleteRoleMenus = roleMenus.stream().filter(item -> deleteSet.contains(item.getMenuId())).toList();
            roleMenuService.removeByIds(deleteRoleMenus);
        }

        // 更新 - 当前 = 新增
        val insertSet = pair.getRight();
        if (!insertSet.isEmpty()) {
            val insertRoleMenus = insertSet.stream().map(id -> {
                val roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(id);
                return roleMenu;
            }).toList();

            roleMenuService.saveBatch(insertRoleMenus);
        }
    }

    @Override
    public void removeRoleById(Long id) {
        val userRoleList = userRoleService.lambdaQuery()
                .eq(UserRole::getRoleId, id)
                .list();

        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        this.removeById(id);
        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery()
                .eq(RoleMenu::getRoleId, id)
        );
    }

    @Override
    public void removeRoleByIds(Collection<Long> ids) {
        val userRoleList = userRoleService.lambdaQuery()
                .in(UserRole::getRoleId, ids)
                .list();

        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        this.removeByIds(ids);

        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery()
                .in(RoleMenu::getRoleId, ids)
        );
    }

    @Override
    public List<RoleVo> getRoleVoList() {
        return roleMapper.selectRoleVoList();
    }

    @Override
    public List<String> getRoleNamesByUserId(Long id) {
        return roleMapper.selectRoleNamesByUserId(id);
    }
}
