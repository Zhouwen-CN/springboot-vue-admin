import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'
import { type BaseVo } from '@/utils/requestTypes'

// 字典类型
export interface DictTypeVo extends BaseVo {
  id: number
  name: string
  dictType: string
}

export interface DictTypeForm {
  id?: number
  name: string
  dictType: string
}

// 获取字典类型分页
export function reqGetDictTypePage() {
  return usePagination<DictTypeVo>('/dict/type')
}

// 新增或者更新字典类型
export function reqSaveDictType(dictTypeForm: DictTypeForm) {
  // 存在id则更新
  if (dictTypeForm.id) {
    return request.put<void, DictTypeForm>('/dict/type', dictTypeForm)
  } else {
    return request.post<void, DictTypeForm>('/dict/type', dictTypeForm)
  }
}

// 删除字典类型
export function reqRemoveDictTypeById(id: number) {
  return request.delete<void>(`/dict/type/${id}`)
}

// 批量删除字典类型
export function reqRemoveDictTypeByIds(ids: number[]) {
  return request.delete<void>('/dict/type', {
    params: { ids: ids + '' }
  })
}

// 字典数据
export interface DictDataVo extends BaseVo {
  id: number
  typeId: number
  label: string
  data: number
  sortId: number
}

export interface DictDataForm {
  id?: number
  typeId: number
  label: string
  data: number
  sortId: number
}

// 获取字典数据分页
export function reqGetDictDataPageByTypeId() {
  return usePagination<DictDataVo>('/dict/data')
}

// 新增或者更新字典数据
export function reqSaveDictData(dictDataForm: DictDataForm) {
  if (dictDataForm.id) {
    return request.put<void, DictDataForm>('/dict/data', dictDataForm)
  } else {
    return request.post<void, DictDataForm>('/dict/data', dictDataForm)
  }
}

// 删除字典数据
export function reqRemoveDictDataById(id: number) {
  return request.delete<void>(`/dict/data/${id}`)
}

// 批量删除字典数据
export function reqRemoveDictDataByIds(ids: number[]) {
  return request.delete<void>('/dict/data', { params: { ids: ids.join() } })
}

// 根据字典类型 id 查询字典列表
export type DictDataSelectorVo = Pick<DictDataVo, 'label' | 'data'>
export function reqGetDictDataListByTypeId(typeId: number) {
  return request.get<DictDataSelectorVo[]>(`/dict/data/${typeId}`)
}
