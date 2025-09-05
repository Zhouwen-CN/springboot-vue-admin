import request from '@/utils/request'
import type { BaseVo } from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'

export interface RoleSelectorVo {
  id: number
  roleName: string
}

/**
 * 获取角色信息
 * @returns
 */
export function reqGetRoles() {
  return request.get<RoleSelectorVo[]>('/role')
}

export interface RoleVo extends RoleSelectorVo, BaseVo {
  description: string
}

/**
 * 获取角色信息，和菜单id列表
 * @returns
 */
export function reqGetRolePage() {
  return usePagination<RoleVo>('/role')
}

export interface RoleForm {
  id?: number
  roleName: string
  description: string
  menuIds: number[]
}

/**
 * 添加或者更新角色信息
 * @param roleForm 角色表单
 * @returns
 */
export function reqSaveRoleMenu(roleForm: RoleForm) {
  if (roleForm.id) {
    return request.put<void, RoleForm>('/role', roleForm)
  } else {
    return request.post<void, RoleForm>('/role', roleForm)
  }
}

/**
 * 删除角色
 * @param id 角色id
 * @returns
 */
export function reqDeleteRole(id: number) {
  return request.delete<void>(`/role/${id}`)
}

/**
 * 批量删除角色
 * @param ids 角色ids
 * @returns
 */
export function reqDeleteRoles(ids: number[]) {
  return request.delete<void>('/role', { params: { ids: ids.join() } })
}

/**
 * 根据用户id，查询角色列表
 * @param userId 用户id
 * @returns
 */
export function reqGetRoleSelectorVoByUserId(userId: number) {
  return request.get<RoleSelectorVo[]>(`/role/${userId}`)
}
