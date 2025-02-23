import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'
import {type CreateAndUpdateTime} from '@/utils/requestTypes'

// 字典类型
export interface DictType extends CreateAndUpdateTime {
  id: number
  name: string
  type: string
}

export type DictTypeForm = Omit<DictType, 'id' | 'createTime' | 'updateTime'> & { id?: number }

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

// 字典数据
export interface DictData extends CreateAndUpdateTime {
  id: number
  typeId: number
  label: string
  value: number
  sort: number
}

export type DictDataForm = Omit<DictData, 'id' | 'createTime' | 'updateTime'> & { id?: number }

// 获取字典数据分页
export function reqGetDictDataPageByTypeId() {
  return usePagination<DictData>('/dict/data')
}

// 新增或者更新字典数据
export function reqSaveDictData(dictDataForm: DictDataForm) {
  if (dictDataForm.id) {
    return request.put<string, DictDataForm>('/dict/data', dictDataForm)
  } else {
    return request.post<string, DictDataForm>('/dict/data', dictDataForm)
  }
}

// 批量删除字典数据
export function reqRemoveDictDataByIds(id: number[]) {
  return request.delete<string, number[]>('/dict/data', {params: {ids: id + ''}})
}

// 根据字典类型查询字典列表
export type DictDataVo = Pick<DictData, 'label' | 'value'>

export function reqGetDictDataListByTypeId(type: string) {
  return request.get<DictDataVo[]>('/dict/data', {
    params: {type}
  })
}
