import type {Menu} from '@/api/auth/user'
import type {RouteRecordSingleViewWithChildren} from 'vue-router'

function getAsyncRoutes(
    modules: Record<string, () => Promise<unknown>>,
    menus: Menu[]
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

export default getAsyncRoutes
