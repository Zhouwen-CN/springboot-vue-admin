import request from '@/api/request'

export interface MenuForm {
  id?: number
  title: string
  accessPath: string
  filePath?: string
  icon: string
  pid: number
}

/**
 * 新增或更新菜单
 * @param menuForm 菜单
 * @returns
 */
export function reqSaveMenu(menuForm: MenuForm) {
  if (menuForm.id) {
    return request.put<any, MenuForm>('/menu', menuForm)
  } else {
    return request.post<any, MenuForm>('/menu', menuForm)
  }
}

export function reqDeleteMenu(id: number) {
  return request.delete(`/menu/${id}`)
}
