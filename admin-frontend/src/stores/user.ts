import type {LoginForm, UserInfo} from '@/api/auth/user'
import {reqLogin, reqLogout, reqRefreshToken} from '@/api/auth/user'
import {type MenuInfo, reqGetMenuList} from '@/api/auth/menu'
import {deleteAsyncRoutes, getAsyncRoutes} from '@/router/asyncRoutes'
import router, {modules} from '@/router'
import {getItem, removeItem, setItem} from '@/utils/localstorageUtil'
import useTagViewStore from '@/stores/tagView'

const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo>(getItem<UserInfo>('USER_INFO', '{}'))
  const menuInfo = ref<MenuInfo[]>(getItem<MenuInfo[]>('MENU_INFO', '[]'))
  let refreshTokenPromise: Promise<boolean> | null = null

  // 登入
  async function doLogin(loginForm: LoginForm) {
    const result = await reqLogin(loginForm)
    userInfo.value = result.data
    setItem('USER_INFO', userInfo.value)
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
        if (result.code === 200) {
          const {accessToken, refreshToken} = result.data
          userInfo.value.accessToken = accessToken
          userInfo.value.refreshToken = refreshToken
          setItem('USER_INFO', userInfo.value)
          resolve(true)
        } else {
          resolve(false)
        }
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
    deleteAsyncRoutes(router)
    useTagViewStore().$reset()
  }

  // 获取菜单信息
  async function getMenuInfo() {
    const roleIds = userInfo.value.roleList.map((role) => role.id)
    const result = await reqGetMenuList(roleIds)
    menuInfo.value = result.data
    setItem('MENU_INFO', menuInfo.value)

    // 加载动态路由
    const routes = getAsyncRoutes(modules, menuInfo.value)
    routes.forEach((route) => {
      router.addRoute('sv-layout', route)
    })
  }

  // 重置仓库
  function $reset() {
    removeItem('USER_INFO')
    removeItem('MENU_INFO')
    userInfo.value = {} as UserInfo
    menuInfo.value = []
  }

  return {userInfo, menuInfo, doLogin, doRefreshToken, doLogout, getMenuInfo, $reset}
})

export default useUserStore
