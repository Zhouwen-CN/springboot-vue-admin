import usePagination from '@/hooks/usePagination'

export interface LoginLogVo {
    id: number
    username: string
    operation: string
    status: string
    ip: string
    userAgent: string
    createTime: string
}

export function reqGetLoginLogPage() {
    return usePagination<LoginLogVo>('/log/login')
}

export interface OperationLogVo {
    id: number
    username: string
    operation: string
    url: string
    method: string
    params: string
    time: number
    status: string
    ip: string
    userAgent: string
}

export function reqGetOperationLogPage() {
    return usePagination<OperationLogVo>('/log/ops')
}

export interface ErrorLogVo {
    id: number
    username: string
    url: string
    method: string
    params: string
    ip: string
    userAgent: string
    errorMsg: string
    createTime: string
}

export function reqGetErrorLogPage() {
    return usePagination<ErrorLogVo>('/log/error')
}
