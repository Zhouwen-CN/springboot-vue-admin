import {type Ref} from 'vue'
import type {ResultData} from '@/api/types'

export interface UseRequestResult<T, D> {
  loading: Ref<boolean>
  data: Ref<T | undefined>
  run: (formData?: D) => Promise<void>
}

/**
 * 请求 hook
 * @param service 请求方法
 * @param onSuccess 成功回调
 * @param onError 错误回调
 * @returns
 */
function useRequest<T = unknown, D = unknown>(
    service: (...args: any) => Promise<ResultData<T>>,
    onSuccess?: (data: T) => void,
    onError?: (err: unknown) => void
): UseRequestResult<T, D> {
  const loading = ref(false)
  const data = ref<T>()

  async function run(...args: any) {
    try {
      loading.value = true
      const res = await service(...args)
      if (onSuccess) {
        onSuccess(res.data)
      } else {
        data.value = res.data
      }
    } catch (err) {
      if (onError) {
        onError(err)
      } else {
        console.warn(err)
      }
    } finally {
      loading.value = false
    }
  }

  return {loading, data, run}
}

export default useRequest
