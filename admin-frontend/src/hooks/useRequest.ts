import {ref, type Ref} from 'vue'
import request from '@/api/request'
import type {AxiosRequestConfig} from 'axios'

export interface UseRequestResult<T> {
  loading: Ref<boolean>
  data: Ref<T | undefined>
    run: () => void
    runAsync: () => Promise<T>
}

/**
 * 请求 hook
 * @param config axios 请求配置
 * @returns
 */
function useRequest<T = unknown, D = unknown>(config: AxiosRequestConfig<D>): UseRequestResult<T> {
  const loading = ref(false)
  const data = ref<T>()

    function run() {
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

    function runAsync() {
        return new Promise<T>((resolve) => {
            run()
            data.value && resolve(data.value)
        })
  }

    return {loading, data, run, runAsync}
}

export default useRequest
