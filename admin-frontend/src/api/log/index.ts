import usePagination from '@/hooks/usePagination'
import type { BaseVo } from '@/utils/requestTypes'

export interface LoginLogVo extends BaseVo {
  id: number
  operation: string
  status: string
  ip: string
  userAgent: string
}

export function reqGetLoginLogPage() {
  return usePagination<LoginLogVo>('/log/login')
}

export interface OperationLogVo extends BaseVo {
  id: number
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

export interface ErrorLogVo extends BaseVo {
  id: number
  url: string
  method: string
  params: string
  ip: string
  userAgent: string
  errorMsg: string
}

export function reqGetErrorLogPage() {
  return usePagination<ErrorLogVo>('/log/error')
}
