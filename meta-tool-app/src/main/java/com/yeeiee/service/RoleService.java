package com.yeeiee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yeeiee.entity.Role;
import com.yeeiee.entity.dto.RoleMenuIdsDto;
import com.yeeiee.entity.vo.RoleMenuVo;

import java.util.Collection;

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
    IPage<RoleMenuVo> getRolePages(Page<RoleMenuVo> page, String searchName);

    /**
     * 添加角色 和 菜单ids
     *
     * @param roleMenuIdsDto 角色 和 菜单ids
     */
    void addRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto);

    /**
     * 修改角色 和 菜单ids
     *
     * @param roleMenuIdsDto 角色 和 菜单ids
     */
    void updateRoleWithMenuIds(RoleMenuIdsDto roleMenuIdsDto);

    /**
     * 根据id删除角色和菜单关系
     *
     * @param id 角色id
     */
    void deleteRole(Long id);

    /**
     * 批量删除角色
     *
     * @param ids 角色ids
     */
    void deleteRoles(Collection<Long> ids);
}
