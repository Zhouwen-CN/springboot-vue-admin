import {createRouter, createWebHashHistory} from 'vue-router'
import request from '@/api/request'
import useUserStore from '@/stores/user'
import defaultRoutes from '@/router/defaultRoutes'
import getAsyncRoutes from '@/router/asyncRoutes'
import type {Menu, UserMenuInfo} from '@/api/auth/user'
import {ElMessage} from 'element-plus'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({showSpinner: false})

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
        const localStorageUserInfo = localStorage.getItem('INFO')
        if (!localStorageUserInfo) {
            ElMessage.warning('路由信息丢失，请重新登录')
            return
        }
        menus = (JSON.parse(localStorageUserInfo) as UserMenuInfo).menus
    }
    const routes = getAsyncRoutes(modules, menus)

    routes.forEach((route) => {
        router.addRoute('Layout', route)
    })
}

// 加载所有动态模块
const modules = import.meta.glob('../views/layout/**/*.vue')

// 如果本地存储中存在菜单，添加路由（页面刷新时）
addRoutes(modules)

// 前置路由守卫
router.beforeEach((to, from, next) => {
    NProgress.start()
    // 取消上一个页面的所有请求
    const pendingRequest = request.getPendingRequest()
    pendingRequest.forEach((cancel, path) => {
        cancel('取消上一个页面未完成请求：' + path)
    })
    pendingRequest.clear()

    // 是否登录
    const userStore = useUserStore()
    if (userStore.token) {
        // 动态加载路由
        addRoutes(modules, userStore.userMenuInfo.menus)

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

// 后置路由守卫
router.afterEach(() => {
    NProgress.done()
})

export default router
