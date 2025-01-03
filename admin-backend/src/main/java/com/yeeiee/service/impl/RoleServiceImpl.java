package com.yeeiee.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.entity.Role;
import com.yeeiee.entity.RoleMenu;
import com.yeeiee.entity.UserRole;
import com.yeeiee.entity.dto.RoleMenuIdsDto;
import com.yeeiee.entity.vo.RoleMenuVo;
import com.yeeiee.exception.DmlOperationException;
import com.yeeiee.mapper.RoleMapper;
import com.yeeiee.mapper.RoleMenuMapper;
import com.yeeiee.mapper.UserRoleMapper;
import com.yeeiee.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
@AllArgsConstructor
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private RoleMapper roleMapper;
    private UserRoleMapper userRoleMapper;
    private RoleMenuMapper roleMenuMapper;

    @Override
    public IPage<RoleMenuVo> getRolePages(Page<RoleMenuVo> page, String searchName) {
        return roleMapper.selectRolePages(page, searchName);
    }

    @Override
    public void addRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto) {
        val exists = roleMapper.exists(new QueryWrapper<Role>()
                .lambda()
                .eq(Role::getRoleName, roleMenuIdsDto.getRoleName())
        );
        if (exists) {
            throw new DmlOperationException("角色名已经存在");
        }

        val role = new Role();
        role.setRoleName(roleMenuIdsDto.getRoleName());
        role.setDesc(roleMenuIdsDto.getDesc());
        roleMapper.insert(role);

        val roleMenuList = roleMenuIdsDto.getMenuIds().stream().map(id -> {
            val roleMenu = new RoleMenu();
            roleMenu.setRoleId(role.getId());
            roleMenu.setMenuId(id);
            return roleMenu;
        }).toList();

        roleMenuMapper.insert(roleMenuList);
    }

    @Override
    public void modifyRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto) {
        val roleId = roleMenuIdsDto.getId();

        // 修改角色
        val role = new Role();
        role.setId(roleId);
        role.setRoleName(roleMenuIdsDto.getRoleName());
        role.setDesc(roleMenuIdsDto.getDesc());
        roleMapper.updateById(role);

        // 获取当前ids 和 更新的ids 做差集
        val roleMenuList = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>()
                .lambda()
                .eq(RoleMenu::getRoleId, roleId)
        );
        val roleMenuMap = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenu::getMenuId));
        val currentSet = roleMenuMap.keySet();
        val updateSet = new HashSet<>(roleMenuIdsDto.getMenuIds());

        // 当前 - 更新 = 删除
        val deleteSet = new HashSet<>(currentSet);
        deleteSet.removeAll(updateSet);
        if (!deleteSet.isEmpty()) {
            val deleteRoleMenus = deleteSet.stream().flatMap(id -> roleMenuMap.get(id).stream()).toList();
            roleMenuMapper.deleteByIds(deleteRoleMenus);
        }

        // 更新 - 当前 = 新增
        val additionSet = new HashSet<>(updateSet);
        additionSet.removeAll(currentSet);
        if (!additionSet.isEmpty()) {
            val additionList = additionSet.stream().map(id -> {
                val roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(id);
                return roleMenu;
            }).toList();

            roleMenuMapper.insert(additionList);
        }
    }

    @Override
    public void removeRole(Long id) {
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .lambda()
                .eq(UserRole::getRoleId, id)
        );
        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        roleMapper.deleteById(id);
        val roleMenuList = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>()
                .lambda()
                .eq(RoleMenu::getRoleId, id)
        );
        roleMenuMapper.deleteByIds(roleMenuList);
    }

    @Override
    public void removeRoles(Collection<Long> ids) {
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .lambda()
                .in(UserRole::getRoleId, ids)
        );
        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        roleMapper.deleteByIds(ids);
        val roleMenuList = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>()
                .lambda()
                .in(RoleMenu::getRoleId, ids)
        );
        roleMenuMapper.deleteByIds(roleMenuList);
    }
}
