import request from '@/utils/request'
import type { BaseVo } from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'

export interface LoginForm {
  username: string
  password: string
}

export interface UserLoginVo {
  id: number
  username: string
  token: string
}

/**
 * 用户登入
 * @param loginForm 登入表单
 * @returns
 */
export function reqLogin(loginForm: LoginForm) {
  return request.post<UserLoginVo, LoginForm>('/login/user', loginForm)
}

/**
 * 刷新 token
 * @returns
 */
export function reqRefreshToken() {
  return request.get<void>('/user/refresh')
}

export function reqLogout() {
  return request.get<void>(`/user/logout`)
}

export interface UserVo extends BaseVo {
  id: number
  username: string
}

/**
 * 获取用户列表，包含角色信息
 * @returns
 */
export function reqGetUserRolePage() {
  return usePagination<UserVo>('/user')
}

export interface UserForm {
  id?: number
  username: string
  password: string
  roleIds: number[]
}

/**
 * 新增用户和角色关系
 * @param userForm 用户和角色关系
 * @returns
 */
export function reqSaveUserRole(userForm: UserForm) {
  if (userForm.id) {
    // 修改
    return request.put<void, UserForm>('/user', userForm)
  } else {
    // 新增
    return request.post<void, UserForm>('/user', userForm)
  }
}

/**
 * 重置密码
 * @param id 用户id
 * @returns
 */
export function reqResetPassword(id: number) {
  return request.patch<void>(`/user/pwd/reset/${id}`)
}

export interface ChangePwdForm {
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
  return request.patch<void, ChangePwdForm>('/user/pwd/change', changePwdForm)
}

/**
 * 删除用户
 * @param id 用户id
 * @returns
 */
export function reqDeleteUser(id: number) {
  return request.delete<void>(`/user/${id}`)
}

/**
 * 批量删除用户
 * @param ids 用户id数组
 * @returns
 */
export function reqDeleteUsers(ids: number[]) {
  return request.delete<void>('/user', { params: { ids: ids.join() } })
}
