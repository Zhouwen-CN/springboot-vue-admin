import {createRouter, createWebHashHistory} from 'vue-router'
import request from '@/api/request'
import useUserStore from '@/stores/user'

const router = createRouter({
    history: createWebHashHistory(import.meta.env.BASE_URL),
    scrollBehavior() {
        return {left: 0, top: 0}
    },
    routes: [
        {
            path: '/',
            name: 'Layout',
            component: () => import('@/views/Layout.vue'),
            redirect: '/home',
            children: [
                {
                    path: 'home',
                    name: 'Home',
                    component: () => import('@/views/Home.vue')
                },
                {
                    path: 'user',
                    name: 'User',
                    component: () => import('@/views/User.vue')
                },
                {
                    path: '404',
                    name: '404',
                    component: () => import('@/views/404.vue')
                }
            ]
        },
        {
            path: '/login',
            name: 'Login',
            component: () => import('@/views/Login.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'NotFound',
            redirect: '/404'
        }
    ]
})

router.beforeEach((to, from, next) => {
    // 遍历pendingRequest，将上一个页面的所有请求cancel掉
    const pendingRequest = request.getPendingRequest()
    pendingRequest.forEach((cancel) => {
        cancel()
    })
    pendingRequest.clear()

    // 判断是否登录
    const userStore = useUserStore()
    if (userStore.token) {
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
