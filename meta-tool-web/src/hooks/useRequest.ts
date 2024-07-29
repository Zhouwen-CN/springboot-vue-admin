import {ref, type Ref} from 'vue'
import request from '@/api/request'
import type {AxiosRequestConfig} from 'axios'

export interface UseRequestResult<T> {
  loading: Ref<boolean>
  refresh: () => void
  data: Ref<T | undefined>
}

/**
 * 请求 hook
 * @param config axios 请求配置
 * @param immediate 是否立即执行请求
 * @returns
 */
function useRequest<T = unknown, D = unknown>(
    config: AxiosRequestConfig<D>,
    immediate: boolean = false
): UseRequestResult<T> {
  const loading = ref(false)
  const data = ref<T>()

  function refresh() {
    loading.value = true
    request
        .request<T, D>(config)
        .then((res) => {
          data.value = res.data
        })
        .catch((err) => {
          console.warn(err)
        })
        .finally(() => {
          loading.value = false
        })
  }

  if (immediate) {
    refresh()
  }
  return {loading, data, refresh}
}

export default useRequest
