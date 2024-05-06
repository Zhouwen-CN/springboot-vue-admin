import {createRouter, createWebHistory} from 'vue-router'
import request from '@/api/request'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
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
    next()
})

export default router
