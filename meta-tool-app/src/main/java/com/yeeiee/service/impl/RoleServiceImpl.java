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
import com.yeeiee.mapper.UserRoleMapper;
import com.yeeiee.service.RoleMenuService;
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
    private RoleMenuService roleMenuService;

    @Override
    public IPage<RoleMenuVo> getRolePages(Page<RoleMenuVo> page, String searchName) {
        return roleMapper.selectRolePages(page, searchName);
    }

    @Override
    public void addRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto) {
        val queryWrapper = new QueryWrapper<Role>().eq("role_name", roleMenuIdsDto.getRoleName());
        val exists = roleMapper.exists(queryWrapper);
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

        roleMenuService.saveBatch(roleMenuList);
    }

    @Override
    public void updateRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto) {
        val roleId = roleMenuIdsDto.getId();

        // 修改角色
        val role = new Role();
        role.setId(roleId);
        role.setRoleName(roleMenuIdsDto.getRoleName());
        role.setDesc(roleMenuIdsDto.getDesc());
        roleMapper.updateById(role);

        // 获取当前ids 和 更新的ids 做差集
        val queryWrapper = new QueryWrapper<RoleMenu>();
        queryWrapper.eq("role_id", roleId);
        val roleMenuList = roleMenuService.list(queryWrapper);
        val roleMenuMap = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenu::getMenuId));
        val currentSet = roleMenuMap.keySet();
        val updateSet = new HashSet<>(roleMenuIdsDto.getMenuIds());

        // 当前 - 更新 = 删除
        val deleteSet = new HashSet<>(currentSet);
        deleteSet.removeAll(updateSet);
        if (!deleteSet.isEmpty()) {
            val deleteRoleMenus = deleteSet.stream().flatMap(id -> roleMenuMap.get(id).stream()).toList();
            roleMenuService.removeBatchByIds(deleteRoleMenus);
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

            roleMenuService.saveBatch(additionList);
        }
    }

    @Override
    public void deleteRole(Long id) {
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("role_id", id));
        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        roleMapper.deleteById(id);
        val roleMenuList = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id", id));
        roleMenuService.removeBatchByIds(roleMenuList);
    }

    @Override
    public void deleteRoles(Collection<Long> ids) {
        val userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().in("role_id", ids));
        if (!userRoleList.isEmpty()) {
            val userIds = userRoleList.stream().map(UserRole::getUserId).toList();
            throw new DmlOperationException("删除失败，尚有用户依赖：" + userIds);
        }

        roleMapper.deleteBatchIds(ids);
        val roleMenuList = roleMenuService.list(new QueryWrapper<RoleMenu>().in("role_id", ids));
        roleMenuService.removeBatchByIds(roleMenuList);
    }
}
