import request from '@/utils/request'
import type {CreateAndUpdateTime} from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'
import type {RoleVo} from '../role'

export interface LoginForm {
  username: string
  password: string
}

type RoleList = RoleVo[]

export interface UserVo {
  id: number
  username: string
  accessToken: string
  refreshToken: string
  roleList: RoleList
}

/**
 * 用户登入
 * @param loginForm 登入表单
 * @returns
 */
export function reqLogin(loginForm: LoginForm) {
  return request.post<UserVo, LoginForm>('/login/user', loginForm)
}

/**
 * 刷新 token
 * @param refreshToken token
 * @returns
 */
export function reqRefreshToken(refreshToken: string) {
  return request.get<UserVo>('/login/refresh', {
    headers: {
      Authorization: `Bearer ${refreshToken}`
    }
  })
}

export function reqLogout(id: number) {
  return request.get<string>(`/user/logout/${id}`)
}

export interface UserRoleVo extends CreateAndUpdateTime {
  id: number
  username: string
  roleList: RoleList
}

/**
 * 获取用户列表，包含角色信息
 * @returns
 */
export function reqGetUserRolePage() {
  return usePagination<UserRoleVo>('/user')
}

export interface UserRoleForm {
  id?: number
  username: string
  password?: string
  roleIds: number[]
}

/**
 * 新增用户和角色关系
 * @param userRoleForm 用户和角色关系
 * @returns
 */
export function reqSaveUserRole(userRoleForm: UserRoleForm) {
  if (userRoleForm.id) {
    // 修改
    return request.put<any, UserRoleForm>('/user', userRoleForm)
  } else {
    // 新增
    return request.post<any, UserRoleForm>('/user', userRoleForm)
  }
}

/**
 * 重置密码
 * @param id 用户id
 * @returns
 */
export function reqResetPassword(id: number) {
  return request.patch<any>(`/user/pwd/reset/${id}`)
}

export interface ChangePwdForm {
  id: number
  oldPwd: string
  newPwd: string
  confirmPwd: string
}

/**
 * 修改密码
 * @param changePwdForm 修改密码表单
 * @returns
 */
export function reqChangePassword(changePwdForm: ChangePwdForm) {
  return request.patch<any, ChangePwdForm>('/user/pwd/change', changePwdForm)
}

/**
 * 删除用户
 * @param id 用户id
 * @returns
 */
export function reqDeleteUser(id: number) {
  return request.delete(`/user/${id}`)
}

/**
 * 批量删除用户
 * @param ids 用户id数组
 * @returns
 */
export function reqDeleteUsers(ids: number[]) {
  return request.delete<any, number[]>('/user', { params: { ids: ids + '' } })
}
