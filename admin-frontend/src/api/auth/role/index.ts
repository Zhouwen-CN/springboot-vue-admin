import request from '@/utils/request'
import type { CreateAndUpdateTime } from '@/utils/requestTypes'
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

export interface RoleVo extends RoleSelectorVo, CreateAndUpdateTime {
  description: string
}

/**
 * 获取角色信息，和菜单id列表
 * @returns
 */
export function reqGetRolePage() {
  return usePagination<RoleVo>('/role')
}

export interface RoleMenuForm {
  id?: number
  roleName: string
  description: string
  menuIds: number[]
}

/**
 * 添加或者更新角色信息
 * @param roleMenuForm 角色表单
 * @returns
 */
export function reqSaveRoleMenu(roleMenuForm: RoleMenuForm) {
  if (roleMenuForm.id) {
    return request.put<void, RoleMenuForm>('/role', roleMenuForm)
  } else {
    return request.post<void, RoleMenuForm>('/role', roleMenuForm)
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
