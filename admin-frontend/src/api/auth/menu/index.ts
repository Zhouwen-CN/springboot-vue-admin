import request from '@/utils/request'
import { type CreateAndUpdateTime } from '@/utils/requestTypes'

export interface MenuVo extends CreateAndUpdateTime {
  id: number
  title: string
  accessPath: string
  filePath: string
  icon: string
  keepAlive: boolean
  pid: number
  type: number
  children: MenuVo[]
}

/**
 * 获取菜单列表
 * @returns
 */
export function reqGetMenuList() {
  return request.get<MenuVo[]>('/menu')
}

export interface MenuForm {
  id?: number
  title: string
  accessPath: string
  filePath?: string
  icon: string
  keepAlive: boolean
  pid: number
  type: number
}

/**
 * 新增或更新菜单
 * @param menuForm 菜单
 * @returns
 */
export function reqSaveMenu(menuForm: MenuForm) {
  if (menuForm.id) {
    return request.put<void, MenuForm>('/menu', menuForm)
  } else {
    return request.post<void, MenuForm>('/menu', menuForm)
  }
}

/**
 * 删除菜单
 * @param id 菜单id
 * @returns
 */
export function reqDeleteMenu(id: number) {
  return request.delete<void>(`/menu/${id}`)
}
