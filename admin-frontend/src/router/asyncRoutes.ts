import type {MenuInfo} from '@/api/auth/menu'
import type {Router, RouteRecordSingleViewWithChildren} from 'vue-router'

/**
 * 给组件注册 name 属性值
 * @param componentName 组件名称
 * @param component 组件
 * @returns
 */
function registerComponentName(componentName: string, component: () => Promise<unknown>) {
  return async () => {
    const res: any = await component()
    res.default.name = componentName
    return res
  }
}

/**
 * 获取并封装异步路由
 * @param modules
 * @param menus
 * @returns
 */
export function getAsyncRoutes(
    modules: Record<string, () => Promise<unknown>>,
    menus: MenuInfo[]
): RouteRecordSingleViewWithChildren[] {
  return menus.map((menu) => {
    const route: RouteRecordSingleViewWithChildren = {
      path: menu.accessPath,
      name: menu.name,
      component: registerComponentName(menu.name, modules[`../views${menu.filePath}`]),
      meta: {
        title: menu.title,
        icon: menu.icon,
        KeepAlive: menu.keepAlive
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
 * @param router 路由器对象
 */
export function deleteAsyncRoutes(router: Router) {
  // 删除默认路由以外的路由
  const deleteNames = router
      .getRoutes()
      .filter((r) => !r.meta.require)
      .map((r) => r.name as string)
  deleteNames.forEach((name) => router.removeRoute(name))
  router.replace('/login')
}

/**
 * 初始化动态路由，在页面刷新时
 * @param router 路由器对象
 * @param modules 模块
 * @returns
 */
export function initAsyncRoutes(router: Router, modules: Record<string, () => Promise<unknown>>) {
  const localStorageUserInfo = localStorage.getItem('MENU_INFO')
  if (!localStorageUserInfo) {
    return
  }

  const menus = JSON.parse(localStorageUserInfo) as MenuInfo[]
  const routes = getAsyncRoutes(modules, menus)
  routes.forEach((route) => {
    router.addRoute('Layout', route)
  })
}
