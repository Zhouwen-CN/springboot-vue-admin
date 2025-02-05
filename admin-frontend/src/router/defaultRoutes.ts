const defaultRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/home',
    meta: {
      icon: 'HomeFilled',
      require: true
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/layout/Home.vue'),
        meta: {
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
          title: '404',
          require: true
        }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/layout/Login.vue'),
    meta: {
      title: '登入',
      require: true
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
