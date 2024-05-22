import request from '@/api/request'
import {type CreateAndUpdateTime} from '@/api/types'

export interface Menu extends CreateAndUpdateTime {
    id: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    pid: number
    children: Menu[]
}

/**
 * 获取菜单列表
 * @returns
 */
export function reqGetMenuList(roleIds: number[]) {
    return request.get<Menu[]>('/menu', {params: {ids: roleIds + ''}})
}

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
