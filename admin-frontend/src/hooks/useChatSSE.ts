import useUserStore from '@/stores/user'
import type { ResultData } from '@/utils/requestTypes'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from '@/stores/setting'
import { deleteAsyncRoutes } from '@/router/asyncRoutes'
import router from '@/router'

const userStore = useUserStore()
type SSEConfig = Pick<RequestInit, 'method' | 'headers' | 'body'>

export interface ChatVo {
  reasoningContent: string
  content: string
}

/**
 * chat sse hook
 * @param lineEscape // 和后端约定好的换行符转义
 * @param spaceEscape // 和后端约定好的空格符转义
 * @returns
 */
function useChatSSE(lineEscape: string = '\\x0a') {
  let baseUrl = import.meta.env.VITE_APP_BASE_URL
  baseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl
  const loading = ref(false)
  let controller = new AbortController()
  const decoder = new TextDecoder()
  let onMessageCallback: (data: ChatVo) => void

  function onMessage(func: (data: ChatVo) => void) {
    onMessageCallback = func
  }

  let onErrorCallback: (message: string) => void

  function onError(func: (message: string) => void) {
    onErrorCallback = func
  }

  function cancel() {
    controller.abort()
  }

  async function run(url: string, config?: SSEConfig) {
    // 刷新token标记
    let refreshFlag = false
    const reuqestInit: RequestInit = {
      method: config?.method ?? 'POST',
      headers: {
        ...config?.headers,
        Accept: 'text/event-stream',
        'Cache-Control': 'no-cache',
        Authorization: `Bearer ${userStore.userInfo.token}`
      },
      body: config?.body,
      signal: controller.signal
    }

    loading.value = true
    try {
      await _run(`${baseUrl}${url}`, reuqestInit)
      // 刷新成功重新请求，刷新失败退出登入
      if (refreshFlag) {
        const isSuccess = await userStore.doRefreshToken()
        if (isSuccess) {
          reuqestInit.headers = {
            ...reuqestInit.headers,
            Authorization: `Bearer ${userStore.userInfo.token}`
          }
          await _run(`${baseUrl}${url}`, reuqestInit)
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
      controller = new AbortController()
      loading.value = false
    }

    // 发送请求
    async function _run(url: string, requestInit: RequestInit) {
      try {
        const response = await fetch(url, requestInit)

        if (!response.ok || !response.body) {
          throw new Error(`http status error: ${response.status}`)
        }

        const type = response.headers.get('content-type')
        // 鉴权失败会返回 application/json 401
        if (!type || type.split(';')[0] === 'application/json') {
          const resultData = (await response.json()) as ResultData<void>
          if (resultData.code === 401) {
            refreshFlag = true
          } else {
            throw new Error(resultData.message)
          }
          // 处理sse
        } else if (!type || type.split(';')[0] === 'text/event-stream') {
          const reader = response.body.getReader()
          if (controller.signal.aborted) {
            reader.cancel()
          }
          await _process(reader)
        } else {
          throw new Error('cannot support content type: ' + type)
        }
      } catch (e: any) {
        controller.abort()
        onErrorCallback?.(e.message)
      }
    }

    // 处理响应，从 @microsoft/fetch-event-source 库抄来的
    async function _process(reader: ReadableStreamDefaultReader<Uint8Array>) {
      let type = ''
      let data = []
      let buffer = ''

      for (;;) {
        const regex = /\r\n|\r|\n/.exec(buffer)

        if (regex) {
          // get next line which can be a new line
          const line = buffer.slice(0, regex.index) || regex[0]
          buffer = buffer.slice(regex.index + regex[0].length)
          const [key, value = ''] = line
            .split(/:(.+)?/, 2)
            .map((v2) => (v2 === null || v2 === void 0 ? void 0 : v2.trimStart())) // send event if line is a new line

          if (line.match(/^(\r\n|\r|\n)$/) && (type || data)) {
            const content = data.join('\n')
            if (type === 'message') {
              const data = content.replaceAll(lineEscape, '\\n')
              onMessageCallback?.(JSON.parse(data) as ChatVo)
            }
            if (type === 'error') {
              throw new Error(content)
            }

            type = ''
            data = []
          } // process line

          switch (key) {
            case 'event': {
              type = value
              break
            }

            case 'data': {
              type = type || 'message'
              data.push(value)
              break
            }
          }
        } else {
          // read next data
          const { value: byteValue, done } = await reader.read()
          if (done) {
            break
          }
          buffer += decoder.decode(byteValue)
        }
      }
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
