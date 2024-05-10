import type {LoginForm, Menu} from '@/api/user'
import {reqLogin, reqUserMenus} from '@/api/user'
import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserStore = defineStore('user', () => {
    const token = ref<string>(localStorage.getItem('token') || '')
    const menus = ref<Menu[]>(JSON.parse(localStorage.getItem('menus') || '[]') as Menu[])

    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        token.value = result.data
        localStorage.setItem('token', result.data)
    }

    async function getUserMenus() {
        const result = await reqUserMenus()
        menus.value = result.data
        localStorage.setItem('menus', JSON.stringify(result.data))
    }

    function $reset() {
        localStorage.removeItem('token')
        localStorage.removeItem('menus')
        token.value = ''
        menus.value = []
    }

    return {token, menus, doLogin, getUserMenus, $reset}
})

export default useUserStore
