import request from '@/api/request'

export interface LoginForm {
    username: string
    password: string
}

export function reqLogin(loginForm: LoginForm) {
    return request.post<string, LoginForm>('/user/login', loginForm)
}

export interface Role {
    id: number
    name: string
}

export interface Menu {
    id: number
    level: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    updateTime: string
    children: Array<Menu>
}

export interface UserInfo {
    id: number
    username: string
    roles: Role[]
    menus: Menu[]
}

export function reqUserMenus() {
    return request.get<UserInfo>('/user/info')
}
