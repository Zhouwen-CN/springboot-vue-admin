import type {LoginForm, UserInfo} from '@/api/user'
import {reqLogin, reqUserInfo} from '@/api/user'
import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserStore = defineStore('user', () => {
    const token = ref<string>(localStorage.getItem('token') || '')
    const userInfo = ref<UserInfo>(JSON.parse(localStorage.getItem('info') || '{}') as UserInfo)

    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        token.value = result.data
        localStorage.setItem('token', result.data)
    }

    async function getUserInfo() {
        const result = await reqUserInfo()
        userInfo.value = result.data
        localStorage.setItem('info', JSON.stringify(result.data))
    }

    function $reset() {
        localStorage.removeItem('token')
        localStorage.removeItem('info')
        token.value = ''
        userInfo.value = {} as UserInfo
    }

    return {token, userInfo, doLogin, getUserInfo, $reset}
})

export default useUserStore
