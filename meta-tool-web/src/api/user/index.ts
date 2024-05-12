import request from '@/api/request'

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

export interface Role {
    id: number
    name: string
}

export interface Menu {
    id: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    pid: number
    updateTime: string
    children: Array<Menu>
}

export interface UserInfo {
    id: number
    username: string
    roles: Role[]
    menus: Menu[]
}

/**
 * 获取用户信息
 * @returns
 */
export function reqUserInfo() {
    return request.get<UserInfo>('/user/info')
}
