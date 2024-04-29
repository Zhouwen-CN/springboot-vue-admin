import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
          path: '/',
          name: 'Home',
          component: () => import('@/views/Home.vue')
      },
      {
          path: '/:pathMatch(.*)*',
          name: '404',
          component: () => import('@/views/404.vue'),
      },
  ]
})

export default router
