import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/layout',
            name: 'Layout',
            component: () => import('@/views/Layout.vue'),
            redirect: '/layout/home',
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
                },
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
            redirect: '/layout/404'
        },
    ]
})

export default router
