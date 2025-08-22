package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.domain.entity.Role;
import com.yeeiee.domain.form.RoleForm;
import com.yeeiee.domain.vo.RoleMenuVo;
import com.yeeiee.domain.vo.RoleVo;

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
     * 获取角色分页（包含菜单ids）
     *
     * @param page       分页配置对象
     * @param searchName 搜索关键词
     * @return 分页结果
     */
    IPage<RoleMenuVo> getRolePage(Page<RoleMenuVo> page, String searchName);

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
     * 获取用户列表
     *
     * @return 用户列表
     */
    List<RoleVo> getRoleVoList();

    /**
     * 根据用户id获取角色名列表
     *
     * @param id user id
     * @return role names
     */
    List<String> getRoleNamesByUserId(Long id);
}
