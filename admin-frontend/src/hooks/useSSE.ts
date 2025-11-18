type SSERequestConfig = Pick<RequestInit, 'method' | 'body' | 'headers'>

export function useSSE() {
  const loading = ref(false)
  let onMessageCallback: (content: string) => void
  let onErrorCallback: (e: unknown) => void
  const abortController = new AbortController()
  function onMessage(func: (content: string) => void) {
    onMessageCallback = func
  }
  function onError(func: (e: unknown) => void) {
    onErrorCallback = func
  }
  function cancel() {
    abortController.abort()
  }

  async function run(url: string, config: SSERequestConfig) {
    loading.value = true
    try {
      const response = await fetch(url, {
        ...config,
        signal: abortController.signal
      })

      if (response.ok && response.body) {
        const readableStream = response.body
        const reader = readableStream.getReader()
        const decoder = new TextDecoder()
        while (true) {
          const { done, value } = await reader.read()
          if (done) {
            break
          }
          const text = decoder.decode(value)
          const content = text
            .split('\n\n')
            .map((item) => item.replace('event:message\ndata:', '').replaceAll('data:', ''))
            .join('')
          if (content) {
            onMessageCallback(content)
          }
        }
      }
    } catch (e) {
      onErrorCallback(e)
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

export default useSSE
