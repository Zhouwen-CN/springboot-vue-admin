import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'

export interface JobVo {
  id: number
  name: string
  handlerName: string
  handlerParam: string
  cronExpression: string
  retryCount: number
  retryInterval: number
  jobEnable: boolean
  createTime: string
  updateTime: string
  loading: boolean
}

// 分页
export function reqGetPage() {
  return usePagination<JobVo>('/job')
}

export interface JobForm {
  id?: number
  name?: string
  cronExpression?: string
  handlerName?: string
  handlerParam?: string
  retryCount: number
  retryInterval: number
}

// 新增 | 修改
export function reqSave(form: JobForm) {
  if (form.id) {
    return request.put<void, JobForm>('/job', form)
  } else {
    return request.post<void, JobForm>('/job', form)
  }
}

// 按照id删除
export function reqRemoveById(id: number, name: string) {
  return request.delete<void>('/job/' + id, {
    params: {
      name
    }
  })
}

interface JobEnableChangeForm {
  name: string
  jobEnable: boolean
}

// 修改任务状态
export function reqModifyEnable(id: number, form: JobEnableChangeForm) {
  return request.patch<void>(`/job/${id}`, form)
}

// 触发一次任务
export function reqTriggerJobOnce(id: number) {
  return request.get<void>(`/job/trigger/${id}`)
}

// 获取任务处理器名称列表
export function reqGetHandlerNames() {
  return request.get<string[]>('/job/handlers')
}

// 任务选择器查询
export interface JobSelectorVo {
  id: number
  name: string
}
export function reqGetJobSelector() {
  return request.get<JobSelectorVo[]>('/job')
}
