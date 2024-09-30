import type {LoginForm, UserInfo} from '@/api/auth/user'
import {reqLogin} from '@/api/auth/user'
import {type MenuInfo, reqGetMenuList} from '@/api/auth/menu'
import {defineStore} from 'pinia'
import {ref} from 'vue'
import {getAsyncRoutes} from '@/router/asyncRoutes'
import router, {modules} from '@/router'

const useUserStore = defineStore('user', () => {
    const userInfo = ref<UserInfo>(JSON.parse(localStorage.getItem('USER_INFO') || '{}') as UserInfo)
    const menuInfo = ref<MenuInfo[]>(
        JSON.parse(localStorage.getItem('MENU_INFO') || '[]') as MenuInfo[]
    )

    // 登入
    async function doLogin(loginForm: LoginForm) {
        let result = await reqLogin(loginForm)
        userInfo.value = result.data
        localStorage.setItem('USER_INFO', JSON.stringify(userInfo.value))
    }

    // 获取菜单信息
    async function getMenuInfo() {
        const roleIds = userInfo.value.roleList.map((role) => role.id)
        const result = await reqGetMenuList(roleIds)
        menuInfo.value = result.data
        localStorage.setItem('MENU_INFO', JSON.stringify(menuInfo.value))

        // 加载动态路由
        const routes = getAsyncRoutes(modules, menuInfo.value)
        routes.forEach((route) => {
            router.addRoute('Layout', route)
        })
    }

    // 重置仓库
    function $reset() {
        localStorage.removeItem('USER_INFO')
        localStorage.removeItem('MENU_INFO')
        userInfo.value = {} as UserInfo
        menuInfo.value = []
    }

    return {userInfo, menuInfo, doLogin, getMenuInfo, $reset}
})

export default useUserStore
