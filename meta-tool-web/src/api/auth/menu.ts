import useRequest from '@/hooks/useRequest'
import request from '@/api/request'

export interface Menu {
    levle: number
    id: number
    title: string
    accessPath: string
    filePath: string
    icon: string
    updateTime: string
    children: Menu[]
}

export function reqGetMenu() {
    return request.get<Menu[]>('/m1/menus')
}

export type MenuItemForm = Omit<Menu, 'children' | 'level' | 'id' | 'updateTime'> & {
    id?: number
    pid?: number
}

export function reqSaveMenuItem(menuItem: MenuItemForm) {
    const url = menuItem.pid ? '/m2' : '/m1'
    const method = menuItem.id ? 'put' : 'post'

    return useRequest({
        url,
        data: menuItem,
        immediate: true,
        method
    })
}
