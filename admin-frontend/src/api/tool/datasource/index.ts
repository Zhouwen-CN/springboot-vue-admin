import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'
import type { BaseVo } from '@/utils/requestTypes'

export interface DataSourceVo extends BaseVo {
  id: number
  name: string
  url: string
  username: string
}

export interface DataSourceForm {
  id?: number
  name: string
  url: string
  username: string
  password: string
}

// 获取数据源配置分页
export function reqGetDataSourcePage() {
  return usePagination<DataSourceVo>('/datasource')
}

export interface DataSourceSelectorVo {
  id: number
  name: string
}

// 查询数据源配置选择器
export function reqGetDataSourceSelectorList() {
  return request.get<DataSourceSelectorVo[]>('/datasource')
}

// 新增｜修改数据源配置
export function reqSaveDataSource(dataSourceForm: DataSourceForm) {
  if (dataSourceForm.id) {
    return request.put<void, DataSourceForm>('/datasource', dataSourceForm)
  } else {
    return request.post<void, DataSourceForm>('/datasource', dataSourceForm)
  }
}

// 删除数据源配置
export function reqRemoveDataSourceById(id: number) {
  return request.delete<void>(`/datasource/${id}`)
}

// 批量删除数据源配置
export function reqRemoveDataSourceByIds(ids: number[]) {
  return request.delete<void>(`/datasource`, { params: { ids: ids.join() } })
}

// 检查连接
export function reqCheckConnection(id: number) {
  return request.get<void>(`/datasource/check/${id}`)
}
