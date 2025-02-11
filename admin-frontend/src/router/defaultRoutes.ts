const defaultRoutes = [
  {
      path: '/',
      name: 'sv-layout',
      component: () => import('@/layout/Layout.vue'),
      redirect: '/home',
      meta: {
          require: true
      },
      children: [
          {
              path: '/home',
              name: 'sv-home',
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
              name: 'sv-404',
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
              name: 'sv-redirect',
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
        name: 'sv-login',
        component: () => import('@/layout/Login.vue'),
        meta: {
            title: '登入',
            require: true,
            hidden: true
        }
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/404',
        meta: {
            require: true
        }
    }
]

export default defaultRoutes
