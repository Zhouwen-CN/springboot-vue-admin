const defaultRoutes = [
    {
        path: '/',
        name: 'layout',
        component: () => import('@/views/Layout.vue'),
        redirect: '/home',
        children: [
            {
                path: '/home',
                name: '/home',
                component: () => import('@/views/Home.vue')
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
        name: '/login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'not found',
        redirect: '/404'
    }
]

export default defaultRoutes
