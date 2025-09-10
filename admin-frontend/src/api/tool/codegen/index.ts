import request from '@/utils/request'
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
  author: string
  ignoreTablePrefix?: string
  ignoreColumnPrefix?: string
  basePackage: string
  tableNames: string[]
}

// 代码生成表导入
export function reqImportCodegenTable(CodegenTableImportForm: CodegenTableImportForm) {
  return request.post<void, CodegenTableImportForm>('/codegen', CodegenTableImportForm)
}
