import usePagination from '@/hooks/usePagination'
import type { BaseVo } from '@/utils/requestTypes'

export interface LoginLogVo extends BaseVo {
  id: number
  operation: string
  status: string
  ip: string
  userAgent: string
}

// 登入日志
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

// 操作日志
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

// 异常日志
export function reqGetErrorLogPage() {
  return usePagination<ErrorLogVo>('/log/error')
}

export interface JobLogVo {
  id: number
  jobName: string
  handlerName: string
  handlerParam: string
  fireNum: number
  time: number
  status: string
  result: string
  startTime: string
  endTime: string
}

export interface JobLogParam {
  jobId?: number
  status?: number
  startTime?: string
  endTime?: string
}

// 调度日志
export function reqGetJobLogPage() {
  return usePagination<JobLogVo>('/log/job')
}
