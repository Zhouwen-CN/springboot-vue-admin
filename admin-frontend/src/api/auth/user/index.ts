import request from '@/api/request'
import type {CreateAndUpdateTime} from '@/api/types'
import usePagination from '@/hooks/usePagination'
import type {Role} from '../role'

export interface LoginForm {
    username: string
    password: string
}

type RoleList = Omit<Role, 'desc' | 'createTime' | 'updateTime'>[]

export interface UserInfo {
    id: number
    username: string
    token: string
    roleList: RoleList
}

/**
 * 用户登入
 * @param loginForm 登入表单
 * @returns
 */
export function reqLogin(loginForm: LoginForm) {
    return request.post<UserInfo, LoginForm>('/user/login', loginForm)
}

export interface UserRoleInfo extends CreateAndUpdateTime {
    id: number
    username: string
    password: string
    roleIds: string
}

/**
 * 获取用户列表，包含角色信息
 * @returns
 */
export function reqGetUserRolePage() {
    return usePagination<UserRoleInfo>('/user')
}

export interface UserRoleForm {
    id?: number
    username: string
    password: string
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
    return request.delete<any, number[]>('/user', {data: ids})
}
