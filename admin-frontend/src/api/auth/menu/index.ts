import request from '@/utils/request'
import {type CreateAndUpdateTime} from '@/utils/requestTypes'

export interface MenuVo extends CreateAndUpdateTime {
    id: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    keepAlive: boolean
    pid: number
    children: MenuVo[]
}

/**
 * 获取菜单列表
 * @returns
 */
export function reqGetMenuList(roleIds: number[]) {
    return request.get<MenuVo[]>('/menu', {params: {ids: roleIds + ''}})
}

export interface MenuForm {
  id?: number
  title: string
  accessPath: string
  filePath?: string
  icon: string
  keepAlive: boolean
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

/**
 * 删除菜单
 * @param id 菜单id
 * @returns
 */
export function reqDeleteMenu(id: number) {
  return request.delete(`/menu/${id}`)
}
