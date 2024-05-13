import request from '@/api/request'
import type {CreateAndUpdateTime} from '@/api/types'
import usePagination from '@/hooks/usePagination'
import type {Role} from '@/api/auth/role'
import useRequest from '@/hooks/useRequest'

export interface LoginForm {
    username: string
    password: string
}

/**
 * 用户登入
 * @param loginForm 登入表单
 * @returns
 */
export function reqLogin(loginForm: LoginForm) {
    return request.post<string, LoginForm>('/user/login', loginForm)
}

export interface Menu extends CreateAndUpdateTime {
    id: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    pid: number
    children: Array<Menu>
}

export interface UserMenuInfo {
    id: number
    username: string
    roles: Role[]
    menus: Menu[]
}

/**
 * 获取用户信息，包括角色信息和权限菜单
 * @returns
 */
export function reqGetUserMenuInfo() {
    return request.get<UserMenuInfo>('/user')
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
    return usePagination<UserRoleInfo>(`/user`)
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
export function reqDeleteUserRole(id: number) {
    // return request.delete(`/user/${id}`)
    return useRequest(
        {
            url: `/user/${id}`,
            method: 'delete'
        },
        true
    )
}
