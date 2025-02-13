import request from '@/utils/request'
import type {CreateAndUpdateTime} from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'

interface RoleCompact {
  id: number
  roleName: string
}

export interface Role extends RoleCompact, CreateAndUpdateTime {
  desc: string
}

/**
 * 获取角色信息
 * @returns
 */
export function reqGetRoles() {
  return request.get<RoleCompact[]>('/role')
}

export interface RoleMenuInfo extends Role {
  menuIds: number[]
}

/**
 * 获取角色信息，和菜单id列表
 * @returns
 */
export function reqGetRolePage() {
  return usePagination<RoleMenuInfo>('/role')
}

export interface RoleMenuForm {
  id?: number
  roleName: string
  desc: string
  menuIds: number[]
}

/**
 * 添加或者更新角色信息
 * @param roleMenuForm 角色表单
 * @returns
 */
export function reqSaveRoleMenu(roleMenuForm: RoleMenuForm) {
  if (roleMenuForm.id) {
    return request.put<any, RoleMenuForm>('/role', roleMenuForm)
  } else {
    return request.post<any, RoleMenuForm>('/role', roleMenuForm)
  }
}

/**
 * 删除角色
 * @param id 角色id
 * @returns
 */
export function reqDeleteRole(id: number) {
  return request.delete(`/role/${id}`)
}

/**
 * 批量删除角色
 * @param ids 角色ids
 * @returns
 */
export function reqDeleteRoles(ids: number[]) {
  return request.delete<any, number[]>('/role', {data: ids})
}
