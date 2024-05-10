import request from '@/api/request'

export interface LoginForm {
    username: string
    password: string
}

export function reqLogin(loginForm: LoginForm) {
    return request.post<string, LoginForm>('/user/login', loginForm)
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

export function reqUserMenus() {
    return request.get<Menu[]>('/user/menus')
}
