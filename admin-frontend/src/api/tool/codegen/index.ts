import request from '@/utils/request'
import type { BaseVo } from '@/utils/requestTypes'
import usePagination from '@/hooks/usePagination'

export interface CodegenTableSelectorVo {
  name: string
  comment: string
}

// 获取代码生成表选择器列表
export function reqGetCodegenTableSelectorList(dataSourceId: number) {
  return request.get<CodegenTableSelectorVo[]>('/codegen', {
    params: {
      dataSourceId
    }
  })
}

// 代码生成表单提交
export interface CodegenTableImportForm {
  dataSourceId?: number
  parentMenuId: number
  author: string
  ignoreTablePrefix?: string
  ignoreColumnPrefix?: string
  tableNames: string[]
}

// 代码生成表导入
export function reqImportCodegenTable(CodegenTableImportForm: CodegenTableImportForm) {
  return request.post<void, CodegenTableImportForm>('/codegen', CodegenTableImportForm)
}

export interface CodegenTableVo extends BaseVo {
  id: number
  dataSource: string
  parentMenuId: number
  tableName: string
  tableComment: string
  className: string
  author: string
  businessName: string
  ignoreTablePrefix: string
  ignoreColumnPrefix: string
}

// 代码生成表分页
export function reqGetCodegenTablePage() {
  return usePagination<CodegenTableVo>('/codegen')
}

// 同步代码生成表字段
export function reqSyncCodegenColumnList(id: number) {
  return request.get<void>(`/codegen/sync/${id}`)
}

// 删除代码生成表
export function reqRemoveCodegenTableById(id: number) {
  return request.delete<void>(`/codegen/${id}`)
}

// 批量删除代码生成表
export function reqRemoveCodegenTableByIds(ids: number[]) {
  return request.delete<void>(`/codegen`, {
    params: {
      ids: ids.join()
    }
  })
}

export interface CodegenColumnVo {
  id: number
  columnName: string
  columnComment: string
  dbType: string
  javaField: string
  javaType: string
  jsType: string
  htmlType: string
  dictTypeId: number
  selectCondition: string
  selectConditionField: boolean
  selectResultField: boolean
  insertField: boolean
  updateField: boolean
}

// 获取代码生成字段列表
export function reqGetCodegenColumnList(id: number) {
  return request.get<CodegenColumnVo[]>(`/codegen/${id}`)
}

export interface CodegenTableForm {
  table: CodegenTableVo
  columns: CodegenColumnVo[]
}

// 修改代码生成表
export function reqUpdateCodegenTable(CodegenTableForm: CodegenTableForm) {
  return request.put<void, CodegenTableForm>(`/codegen`, CodegenTableForm)
}
