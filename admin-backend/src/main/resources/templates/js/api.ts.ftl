<#--@formatter:off-->
import usePagination from '@/hooks/usePagination'
import request from '@/utils/request'

export interface ${table.className}Vo {
<#list columns?filter(column -> column.selectResultField) as column>
  ${column.columnName}: ${column.jsType}
</#list>
}

// 分页
export function reqGetPage() {
  return usePagination<${table.className}Vo>('/${table.businessName}')
}

// 按照id查询
export function reqGetById(id: number) {
  return request.get<${table.className}Vo>('/${table.businessName}/' + id)
}

export interface ${table.className}Form {
<#list columns?filter(column -> column.updateField) as column>
  ${column.columnName}?: ${column.jsType}
</#list>
}

// 新增 | 修改
export function reqSave(form: ${table.className}Form) {
  if (form.id) {
    return request.put<void, ${table.className}Form>('/${table.businessName}', form)
  } else {
    return request.post<void, ${table.className}Form>('/${table.businessName}', form)
  }
}

// 按照id删除
export function reqRemoveById(id: number) {
  return request.delete<void>('/${table.businessName}/' + id)
}

// 批量删除
export function reqRemoveByIds(ids: number[]) {
  return request.delete<void>(`/${table.businessName}`, { params: { ids: ids.join() } })
}