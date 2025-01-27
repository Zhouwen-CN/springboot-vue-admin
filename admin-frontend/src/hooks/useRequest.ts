import {type Ref} from 'vue'
import type {ResultData} from '@/api/types'

export interface UseRequestResult<T> {
  loading: Ref<boolean>
  data: Ref<T | undefined>
  onSuccess: (callback: (data: T) => void) => void
  onError: (callback: (err: unknown) => void) => void
  run: (...args: any) => Promise<void>
}

/**
 * 请求 hook
 * @param service 请求方法
 * @returns
 */
function useRequest<T = unknown>(
    service: (...args: any) => Promise<ResultData<T>>
): UseRequestResult<T> {
  const loading = ref(false)
  const data = ref<T>()
  let onSuccess: ((data: T) => void) | null = null
  let onError: ((err: unknown) => void) | null = null

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

  return {
    loading,
    data,
    run,
    onSuccess: (callback: (data: T) => void) => {
      onSuccess = callback
    },
    onError: (callback: (err: unknown) => void) => {
      onError = callback
    }
  }
}

export default useRequest
