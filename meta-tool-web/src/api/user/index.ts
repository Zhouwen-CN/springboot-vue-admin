import request from '@/api/request'

export interface LoginForm {
    username: string
    password: string
}

export function reqLogin(loginForm: LoginForm) {
    return request.post<string, LoginForm>('/login', loginForm)
}
