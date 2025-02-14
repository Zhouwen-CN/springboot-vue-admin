import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'

export interface DictType {
  id: number
  name: string
  type: string
  enable: boolean
}

export type DictTypeForm = Omit<DictType, 'id'> & { id?: number }

// 获取字典类型分页
export function reqGetDictTypePage() {
  return usePagination<DictType>('/dict/type')
}

// 新增或者更新字典类型
export function reqSaveDictType(dictTypeForm: DictTypeForm) {
  // 存在id则更新
  if (dictTypeForm.id) {
    return request.put<string, DictTypeForm>('/dict/type', dictTypeForm)
  } else {
    return request.post<string, DictTypeForm>('/dict/type', dictTypeForm)
  }
}

// 删除字典类型
export function reqRemoveDictTypeById(id: number) {
  return request.delete<string, number>(`/dict/type/${id}`)
}

// 批量删除字典类型
export function reqRemoveDictTypeByIds(ids: number[]) {
  return request.delete<string, number[]>('/dict/type', {
    params: {ids: ids + ''}
  })
}
