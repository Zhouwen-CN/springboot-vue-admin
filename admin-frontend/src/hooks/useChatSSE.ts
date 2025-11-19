import {type EventSourceMessage, fetchEventSource, type FetchEventSourceInit} from '@microsoft/fetch-event-source'
import {baseConfig} from '@/utils/request'
import useUserStore from '@/stores/user'
import type {ResultData} from '@/utils/requestTypes'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from '@/stores/setting'
import {deleteAsyncRoutes} from '@/router/asyncRoutes'
import router from '@/router'

const userStore = useUserStore()

type FetchEventSourceConfig = Pick<FetchEventSourceInit, 'method' | 'body' | 'headers'>
export function useChatSSE() {
  // loading
  const loading = ref(false)
  // 取消控制
  let abortController = new AbortController()
  // 回调
  let onMessageCallback: (ev: EventSourceMessage) => void
  // 消息处理回调
  function onMessage(func: (ev: EventSourceMessage) => void) {
    onMessageCallback = func
  }
  // 取消请求
  function cancel() {
    abortController.abort()
  }

  // 执行请求
  async function run(url: string, config?: FetchEventSourceConfig) {
    loading.value = true
    // 刷新token标记
    let refreshFlag = false
    if (!config) {
      config = {}
    }
    config.headers = {
      ...config.headers,
      Authorization: `Bearer ${userStore.userInfo.token}`
    }

    const requestConfig = {
      method: 'POST',
      signal: abortController.signal,
      onmessage: async (ev: EventSourceMessage) => {
        if (ev.event === 'message') {
          onMessageCallback(ev)
        } else if (ev.event === 'error') {
          abortController.abort()
          const error = JSON.parse(ev.data) as ResultData<void>
          if (error.code === 401) {
            refreshFlag = true
          } else {
            ElMessage.error(error.message)
          }
        }
      },
      onerror: (err: any) => {
        console.warn('sse request error', err)
        abortController.abort()
      },
      ...config
    }
    try {
      // 执行请求
      await fetchEventSource(`${baseConfig.baseURL}${url}`, requestConfig)
      // 刷新token，成功重新请求，失败退出登入
      if (refreshFlag) {
        const isSuccess = await userStore.doRefreshToken()
        if (isSuccess) {
          requestConfig.headers!.Authorization = `Bearer ${userStore.userInfo.token}`
          await fetchEventSource(`${baseConfig.baseURL}${url}`, requestConfig)
        } else {
          useUserStore().$reset()
          useTagViewStore().$reset()
          useSettingStore().$reset()
          deleteAsyncRoutes(router)
        }
      }
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
    cancel
  }
}

export default useChatSSE
