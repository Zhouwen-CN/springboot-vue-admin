import type {Menu} from '@/api/auth/menu'
import type {Router, RouteRecordSingleViewWithChildren} from 'vue-router'
import useUserStore from '@/stores/user'

/**
 * 获取异步路由
 * @param modules
 * @param menus
 * @returns
 */
export function getAsyncRoutes(
    modules: Record<string, () => Promise<unknown>>,
    menus: Menu[] = []
): RouteRecordSingleViewWithChildren[] {
  return menus.map((menu) => {
    const route: RouteRecordSingleViewWithChildren = {
      path: menu.accessPath,
      name: menu.accessPath,
      component: modules[`../views${menu.filePath}`],
      meta: {
        title: menu.title,
        icon: menu.icon
      },
      children: []
    }

    if (menu.children && menu.children.length !== 0) {
      route.component = undefined
      route.redirect = menu.children[0].accessPath
      route.children = getAsyncRoutes(modules, menu.children)
    }

    return route
  })
}

/**
 * 删除异步路由并退出登入
 * @param router 路由对象
 * @param userStore 用户状态存储对象
 */
export function deleteAsyncRoutesAndExit(
    router: Router,
    userStore: ReturnType<typeof useUserStore>
) {
  // 删除默认路由以外的路由
  const deleteNames = router
      .getRoutes()
      .filter((r) => !r.meta.require)
      .map((r) => r.name as string)
  deleteNames.forEach((name) => router.removeRoute(name))
  userStore.$reset()
  router.replace('/login')
}
