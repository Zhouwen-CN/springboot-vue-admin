import usePagination from '@/hooks/usePagination'

export interface LoginLog {
  id: number
  username: string
  operation: string
  status: string
  ip: string
  userAgent: string
  createTime: string
}

export function reqGetLoginLogPage() {
  return usePagination<LoginLog>('/log/login', [5, 7, 9, 11], 11)
}

export interface OperationLog {
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
  return usePagination<OperationLog>('/log/ops', [5, 7, 9, 11], 11)
}

export interface ErrorLog {
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
  return usePagination<ErrorLog>('/log/error', [5, 7, 9, 11], 11)
}
