import {defineStore} from 'pinia'
import {type Menu, reqGetMenu} from '@/api/auth/menu'
import {ref} from 'vue'

const useMenuStore = defineStore('menu', () => {
    const menus = ref<Menu[]>(JSON.parse(localStorage.getItem('menus') || '[]') as Menu[])

    async function doGetMenu() {
        const menuResult = await reqGetMenu()
        menus.value = menuResult.data
        localStorage.setItem('menus', JSON.stringify(menuResult.data))
    }

    return {menus, doGetMenu}
})

export default useMenuStore
