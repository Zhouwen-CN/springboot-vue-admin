import type { LoginForm, UserVo } from '@/api/auth/user'
import { reqLogin, reqLogout, reqRefreshToken } from '@/api/auth/user'
import { type MenuVo, reqGetMenuList } from '@/api/auth/menu'
import { deleteAsyncRoutes, getAsyncRoutes } from '@/router/asyncRoutes'
import router, { modules } from '@/router'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from './setting'

export const storeName = 'PINIA:USER-STORE'

const useUserStore = defineStore(
  storeName,
  () => {
    const userInfo = ref<UserVo>({} as UserVo)
    const menuInfo = ref<MenuVo[]>([])
    let refreshTokenPromise: Promise<boolean> | null = null

    // 登入
    async function doLogin(loginForm: LoginForm) {
      const result = await reqLogin(loginForm)
      userInfo.value = result.data
    }

    // 刷新 token
    async function doRefreshToken() {
      if (refreshTokenPromise) {
        return refreshTokenPromise
      }
      // eslint-disable-next-line no-async-promise-executor
      refreshTokenPromise = new Promise(async (resolve) => {
        try {
          // 状态码!=200，拦截器会返回一个失败的promise
          const result = await reqRefreshToken(userInfo.value.refreshToken)
          const { accessToken, refreshToken } = result.data
          userInfo.value.accessToken = accessToken
          userInfo.value.refreshToken = refreshToken
          resolve(true)
        } catch (error) {
          resolve(false)
        }
      })

      refreshTokenPromise.finally(() => {
        refreshTokenPromise = null
      })
      return refreshTokenPromise
    }

    // 登出
    async function doLogout() {
      await reqLogout(userInfo.value.id)
      $reset()
      useTagViewStore().$reset()
      useSettingStore().$reset()
      deleteAsyncRoutes(router)
    }

    // 获取菜单信息
    async function getMenuInfo() {
      const roleIds = userInfo.value.roleList.map((role) => role.id)
      const result = await reqGetMenuList(roleIds)
      menuInfo.value = result.data

      // 加载动态路由
      const routes = getAsyncRoutes(modules, menuInfo.value)
      routes.forEach((route) => {
        router.addRoute('sv-layout', route)
      })
    }

    // 重置仓库
    function $reset() {
      userInfo.value = {} as UserVo
      menuInfo.value = []
    }

    return { userInfo, menuInfo, doLogin, doRefreshToken, doLogout, getMenuInfo, $reset }
  },
  {
    persist: {
      paths: ['userInfo', 'menuInfo']
    }
  }
)

export default useUserStore
