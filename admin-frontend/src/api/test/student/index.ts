import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'

export interface StudentTestVo {
  id: number
  name: string
  gender: number
  birthday: string
  intro: string
  create_time: string
  update_time: string
}

// 分页
export function reqGetPage() {
  return usePagination<StudentTestVo>('/student')
}

// 按照id查询
export function reqGetById(id: number) {
  return request.get<StudentTestVo>('/student/' + id)
}

export interface StudentTestForm {
  id?: number
  name?: string
  gender?: number
  birthday?: string
  intro?: string
}

// 新增 | 修改
export function reqSave(form: StudentTestForm) {
  if (form.id) {
    return request.put<void, StudentTestForm>('/student', form)
  } else {
    return request.post<void, StudentTestForm>('/student', form)
  }
}

// 按照id删除
export function reqRemoveById(id: number) {
  return request.delete<void>('/student/' + id)
}

// 批量删除
export function reqRemoveByIds(ids: number[]) {
  return request.delete<void>(`/student`, { params: { ids: ids.join() } })
}
