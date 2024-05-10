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
            name: 'layout',
            component: () => import('@/views/Layout.vue'),
            redirect: '/home',
            children: [
                {
                    path: '/home',
                    name: 'home',
                    component: () => import('@/views/layout/home/index.vue')
                },
                {
                    path: '/404',
                    name: '404',
                    component: () => import('@/views/404.vue')
                }
            ]
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/Login.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            name: 'not found',
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

    // 是否登录
    const userStore = useUserStore()
    if (userStore.token) {
        // 动态加载路由
        if (userStore.menus?.length !== 0) {
            userStore.menus.forEach((prentMenu) => {
                const prentAccessPath = prentMenu.accessPath

                // 存在二级菜单
                if (prentMenu.children?.length !== 0) {
                    const children = prentMenu.children.map((childMenu) => {
                        let childAccessPath = childMenu.accessPath
                        return {
                            path: childAccessPath,
                            name: childAccessPath,
                            component: () => import(/* @vite-ignore */ `../views${childMenu.filePath}`)
                        }
                    })

                    // 父路由不存在则添加
                    if (!router.hasRoute(prentAccessPath)) {
                        router.addRoute('layout', {
                            path: prentAccessPath,
                            name: prentAccessPath,
                            redirect: prentMenu.children[0].accessPath,
                            children
                        })
                    }

                    // 不存在二级菜单
                } else {
                    if (!router.hasRoute(prentAccessPath)) {
                        router.addRoute('layout', {
                            path: prentAccessPath,
                            name: prentAccessPath,
                            component: () => import(/* @vite-ignore */ `../views${prentMenu.filePath}`),
                            children: []
                        })
                    }
                }
            })
        } else {
            userStore.getUserMenus()
        }

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
