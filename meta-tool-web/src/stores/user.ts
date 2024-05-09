import type {LoginForm} from '@/api/user'
import {reqLogin} from '@/api/user'
import {defineStore} from 'pinia'
import {ref} from 'vue'

const useUserStore = defineStore('user', () => {
    const token = ref<string>(localStorage.getItem('token') || '')

    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        token.value = result.data
        localStorage.setItem('token', result.data)
    }

    return {token, doLogin}
})

export default useUserStore
