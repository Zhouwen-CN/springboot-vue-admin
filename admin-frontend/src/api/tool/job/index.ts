import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'

export interface JobVo {
  id: number
  name: string
  cronExpression: string
  jsScript: string
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
  jsScript?: string
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
export function reqRemoveById(id: number) {
  return request.delete<void>('/job/' + id)
}

// 批量删除
export function reqRemoveByIds(ids: number[]) {
  return request.delete<void>(`/job`, { params: { ids: ids.join() } })
}

// 修改任务状态
export function reqModifyEnable(id: number, jobEnable: boolean) {
  return request.patch<void>(`/job/${id}`, null, {
    params: { jobEnable: jobEnable }
  })
}
