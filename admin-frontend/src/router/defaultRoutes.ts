const defaultRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/home',
    meta: {
      require: true
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/layout/Home.vue'),
        meta: {
          icon: 'HomeFilled',
          title: '首页',
          require: true,
          affix: true
        }
      },
      {
        path: '/404',
        name: '404',
        component: () => import('@/layout/404.vue'),
        meta: {
            icon: 'WarningFilled',
            title: '404',
            require: true,
            hidden: true
        }
      },
      {
        path: '/redirect/:path(.*)',
        name: 'Redirect',
        meta: {
          title: '重定向',
          require: true,
          hidden: true
        },
        component: () => import('@/layout/Redirect.vue')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/layout/Login.vue'),
    meta: {
        title: '登入',
        require: true,
        hidden: true
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    redirect: '/404',
    meta: {
      require: true
    }
  }
]

export default defaultRoutes
