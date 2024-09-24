import {createRouter, createWebHashHistory} from 'vue-router'
import request from '@/api/request'
import useUserStore from '@/stores/user'
import defaultRoutes from '@/router/defaultRoutes'
import {initAsyncRoutes} from '@/router/asyncRoutes'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({showSpinner: false})

// 加载所有动态模块
const modules = import.meta.glob('../views/layout/**/*.vue')

// 基础路由配置
const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    scrollBehavior() {
        return {left: 0, top: 0}
    },
    routes: defaultRoutes
})

// 初始化动态路由（页面刷新时）
initAsyncRoutes(router, modules)

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
    const token = userStore.userInfo.token
    if (token) {
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
export {modules}
