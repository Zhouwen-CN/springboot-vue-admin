import { createRouter, createWebHashHistory } from 'vue-router'
import useUserStore from '@/stores/user'
import defaultRoutes from '@/router/defaultRoutes'
import { initAsyncRoutes } from '@/router/asyncRoutes'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import useTagViewStore, { type TagView } from '@/stores/tagView'

NProgress.configure({ showSpinner: false })

// 加载所有动态模块
const modules = import.meta.glob('../views/**/*.vue')

// 基础路由配置
const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  scrollBehavior() {
    return { left: 0, top: 0 }
  },
  routes: defaultRoutes
})

// 初始化动态路由（页面刷新时）
initAsyncRoutes(router, modules)

// 前置路由守卫
router.beforeEach((to, _, next) => {
  NProgress.start()

  // 是否登录
  const userStore = useUserStore()
  const token = userStore.userInfo.accessToken
  if (token) {
    // 登入逻辑
    if (to.path === '/login') {
      next({ path: '/home' })
    } else {
      next()
    }
  } else {
    if (to.path === '/login') {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.path } })
    }
  }
})

// 后置路由守卫
router.afterEach((to) => {
  // 添加路由到状态
  if (!to.meta.hidden) {
    const tagViewStore = useTagViewStore()
    const tagView: TagView = {
      path: to.path,
      fullPath: to.fullPath,
      name: to.name as string,
      query: to.query,
      title: to.meta.title as string,
      affix: to.meta?.affix as boolean,
      keepAlive: to.meta?.keepAlive as boolean
    }
    tagViewStore.addView(tagView)
  }

  // 设置 title
  if (to.meta.title) {
    document.title = `SV Admin | ${to.meta.title}`
  } else {
    document.title = 'SV Admin'
  }

  NProgress.done()
})

export default router
export { modules }
