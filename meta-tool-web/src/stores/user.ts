import type {LoginForm, UserMenuInfo} from '@/api/auth/user'
import {reqGetUserMenuInfo, reqLogin} from '@/api/auth/user'
import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserStore = defineStore('user', () => {
    const token = ref<string>(localStorage.getItem('TOKEN') || '')
    const userMenuInfo = ref<UserMenuInfo>(
        JSON.parse(localStorage.getItem('INFO') || '{}') as UserMenuInfo
    )

    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        token.value = result.data
        localStorage.setItem('TOKEN', result.data)
    }

    async function getUserMenuInfo() {
        const result = await reqGetUserMenuInfo()
        userMenuInfo.value = result.data
        localStorage.setItem('INFO', JSON.stringify(result.data))
    }

    function $reset() {
        localStorage.removeItem('TOKEN')
        localStorage.removeItem('INFO')
        token.value = ''
        userMenuInfo.value = {} as UserMenuInfo
    }

    return {token, userMenuInfo, doLogin, getUserMenuInfo, $reset}
})

export default useUserStore
