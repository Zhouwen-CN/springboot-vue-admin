const defaultRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/home',
    meta: {
      title: '首页',
      icon: 'HomeFilled',
      require: true
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/layout/Home.vue'),
        meta: {
          require: true
        }
      },
      {
        path: '/404',
        name: '404',
        component: () => import('@/layout/404.vue'),
        meta: {
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
