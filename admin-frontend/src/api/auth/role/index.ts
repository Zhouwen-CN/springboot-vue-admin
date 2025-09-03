import request from '@/utils/request'
import type { CreateAndUpdateTime } from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'

export interface RoleVo {
  id: number
  roleName: string
}

/**
 * 获取角色信息
 * @returns
 */
export function reqGetRoles() {
  return request.get<RoleVo[]>('/role')
}

export interface RoleMenuVo extends RoleVo, CreateAndUpdateTime {
  description: string
  menuIds: number[]
}

/**
 * 获取角色信息，和菜单id列表
 * @returns
 */
export function reqGetRolePage() {
  return usePagination<RoleMenuVo>('/role')
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
