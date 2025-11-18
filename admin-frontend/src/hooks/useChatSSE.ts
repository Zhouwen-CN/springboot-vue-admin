import {
  fetchEventSource,
  type FetchEventSourceInit,
  type EventSourceMessage
} from '@microsoft/fetch-event-source'
import { baseConfig } from '@/utils/request'
import useUserStore from '@/stores/user'
import type { ResultData } from '@/utils/requestTypes'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from '@/stores/setting'
import { deleteAsyncRoutes } from '@/router/asyncRoutes'
import router from '@/router'
const userStore = useUserStore()

type FetchEventSourceConfig = Pick<FetchEventSourceInit, 'method' | 'body' | 'headers'>
export function useChatSSE() {
  // loading
  const loading = ref(false)
  // 取消控制
  const abortController = new AbortController()
  // 回调
  let onMessageCallback: (ev: EventSourceMessage) => void
  let onErrorCallback: (err: any) => number | null | undefined | void
  // 请求配置缓存
  let cachedUrl: string
  let cachedConfig: FetchEventSourceInit
  // 消息处理回调
  function onMessage(func: (ev: EventSourceMessage) => void) {
    onMessageCallback = func
  }

  // 错误回调
  function onError(func: (err: any) => number | null | undefined | void) {
    onErrorCallback = func
  }

  // 取消请求
  function cancel() {
    abortController.abort()
  }

  // 执行请求
  async function run(url: string, config?: FetchEventSourceConfig) {
    loading.value = true
    const headers = config?.headers
    delete config?.headers

    cachedUrl = `${baseConfig.baseURL}${url}`
    cachedConfig = {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${userStore.userInfo.accessToken}`,
        ...headers
      },
      signal: abortController.signal,
      onmessage: async (ev: EventSourceMessage) => {
        if (ev.event === 'error') {
          const error = JSON.parse(ev.data) as ResultData<void>

          // 401需要刷新token
          if (error.code === 401) {
            const isSuccess = await userStore.doRefreshToken()
            // 刷新成功重新请求
            if (isSuccess) {
              run(cachedUrl, cachedConfig)
              // 刷新失败直接退出登入
            } else {
              useUserStore().$reset()
              useTagViewStore().$reset()
              useSettingStore().$reset()
              deleteAsyncRoutes(router)
              ElMessage.error(error.message)
            }
          } else {
            ElMessage.error(error.message)
          }
        }
        if (ev.event === 'message') {
          onMessageCallback(ev)
        }
      },
      onerror: onErrorCallback,
      ...config
    }

    try {
      // 执行请求
      await fetchEventSource(cachedUrl, cachedConfig)
    } catch (e) {
      // do noting
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    run,
    onMessage,
    onError,
    cancel
  }
}

export default useChatSSE
