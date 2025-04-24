import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'
import {type CreateAndUpdateTime} from '@/utils/requestTypes'

// 字典类型
export interface DictTypeVo extends CreateAndUpdateTime {
    id: number
    name: string
    type: string
}

export type DictTypeForm = Omit<DictTypeVo, 'id' | 'createTime' | 'updateTime'> & { id?: number }

// 获取字典类型分页
export function reqGetDictTypePage() {
    return usePagination<DictTypeVo>('/dict/type')
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
export interface DictDataVo extends CreateAndUpdateTime {
    id: number
    typeId: number
    label: string
    value: number
    sort: number
}

export type DictDataForm = Omit<DictDataVo, 'id' | 'createTime' | 'updateTime'> & { id?: number }

// 获取字典数据分页
export function reqGetDictDataPageByTypeId() {
    return usePagination<DictDataVo>('/dict/data')
}

// 新增或者更新字典数据
export function reqSaveDictData(dictDataForm: DictDataForm) {
  if (dictDataForm.id) {
    return request.put<string, DictDataForm>('/dict/data', dictDataForm)
  } else {
    return request.post<string, DictDataForm>('/dict/data', dictDataForm)
  }
}

// 删除字典数据
export function reqRemoveDictDataById(id: number) {
  return request.delete<string, number>(`/dict/data/${id}`)
}

// 批量删除字典数据
export function reqRemoveDictDataByIds(ids: number[]) {
    return request.delete<string, number[]>('/dict/data', {params: {ids: ids + ''}})
}

// 根据字典类型查询字典列表
export type LabelValueVo = Pick<DictDataVo, 'label' | 'value'>

export function reqGetDictDataListByTypeId(type: string) {
    return request.get<LabelValueVo[]>('/dict/data', {
        params: {type}
    })
}
