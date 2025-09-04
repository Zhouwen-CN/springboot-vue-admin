package com.yeeiee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.Role;
import com.yeeiee.domain.form.RoleForm;
import com.yeeiee.domain.vo.RoleSelectorVo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author chen
 * @since 2024-05-09
 */
public interface RoleService extends IService<Role> {
    /**
     * 添加角色 和 菜单ids
     *
     * @param roleForm 角色 和 菜单ids
     */
    void addRole(RoleForm roleForm);

    /**
     * 修改角色 和 菜单ids
     *
     * @param roleForm 角色 和 菜单ids
     */
    void modifyRole(RoleForm roleForm);

    /**
     * 根据id删除角色和菜单关系
     *
     * @param id 角色id
     */
    void removeRoleById(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 角色ids
     */
    void removeRoleByIds(Collection<Long> ids);

    /**
     * 获取角色列表
     *
     * @return 角色列表
     */
    List<RoleSelectorVo> getRoleVoList();

    /**
     * 根据用户id获取角色列表
     * @param userId 用户id
     * @return 角色列表
     */
    List<RoleSelectorVo> getRoleSelectorVoListByUserId(Long userId);
}
