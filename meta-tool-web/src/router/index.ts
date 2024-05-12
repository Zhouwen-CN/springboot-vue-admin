import {createRouter, createWebHashHistory} from 'vue-router'
import request from '@/api/request'
import useUserStore from '@/stores/user'
import defaultRoutes from '@/router/defaultRoutes'
import getAsyncRoutes from '@/router/asyncRoutes'
import type {Menu, UserInfo} from '@/api/user'

// 基础路由配置
const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    scrollBehavior() {
        return {left: 0, top: 0}
    },
    routes: defaultRoutes
})

// 添加路由方法
function addRoutes(modules: Record<string, () => Promise<unknown>>, menus?: Menu[]) {
    if (!menus) {
        const localStorageUserInfo = localStorage.getItem('info') || '{}'
        menus = (JSON.parse(localStorageUserInfo) as UserInfo).menus
        if (!menus) {
            return
        }
    }
    const routes = getAsyncRoutes(modules, menus)
    routes.forEach((route) => {
        if (!router.hasRoute(route.name as string)) {
            router.addRoute('layout', route)
        }
    })
}

// 加载所有动态模块
const modules = import.meta.glob('../views/layout/**/*.vue')

// 如果本地存储中存在菜单，添加路由（页面刷新时）
addRoutes(modules)

router.beforeEach((to, from, next) => {
    // 取消上一个页面的所有请求
    const pendingRequest = request.getPendingRequest()
    pendingRequest.forEach((cancel) => {
        cancel()
    })
    pendingRequest.clear()

    // 是否登录
    const userStore = useUserStore()
    if (userStore.token) {
        // 动态加载路由
        if (userStore.userInfo.menus?.length === 0) {
            userStore.getUserInfo()
        }
        addRoutes(modules, userStore.userInfo.menus)

        // 登入逻辑
        if (to.path === '/login') {
            next({path: '/home'})
        } else {
            next()
        }
    } else {
        if (to.path === '/login') {
            next()
        } else {
            next({path: '/login', query: {redirect: to.path}})
        }
    }
})

export default router
