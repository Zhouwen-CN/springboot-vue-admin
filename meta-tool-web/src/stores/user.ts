import type {LoginForm, UserMenuInfo} from '@/api/auth/user'
import {reqLogin} from '@/api/auth/user'
import {reqGetMenuList} from '@/api/auth/menu'
import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserStore = defineStore('user', () => {
    const userMenuInfo = ref<UserMenuInfo>(
        JSON.parse(localStorage.getItem('USER_INFO') || '{}') as UserMenuInfo
    )

    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        userMenuInfo.value = result.data
        localStorage.setItem('USER_INFO', JSON.stringify(userMenuInfo.value))
    }

    async function getUserMenuInfo() {
        const result = await reqGetMenuList(userMenuInfo.value.roleIds)
        userMenuInfo.value.menus = result.data
        localStorage.setItem('USER_INFO', JSON.stringify(userMenuInfo.value))
    }

    function $reset() {
        localStorage.removeItem('USER_INFO')
        userMenuInfo.value = {} as UserMenuInfo
    }

    return {userMenuInfo, doLogin, getUserMenuInfo, $reset}
})

export default useUserStore
