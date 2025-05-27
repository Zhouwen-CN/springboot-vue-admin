import { type Ref } from 'vue'
import request from '@/utils/request'
import type { Page } from '@/utils/requestTypes'
import type { AxiosRequestConfig } from 'axios'

export interface PaginationResult<T> {
  loading: Ref<boolean>
  current: Ref<number>
  total: Ref<number>
  size: Ref<number>
  sizeOption: Array<number>
  data: Ref<T[]>
  refresh: (config?: AxiosRequestConfig) => void
  onPageChange: (pageNumber: number, config?: AxiosRequestConfig) => void
  onSizeChange: (pageSize: number, config?: AxiosRequestConfig) => void
}

/**
 * 走 get 请求，并且不会立即请求
 * 后端请求接口格式保持一致：${baseUrl}/${size.value}/${current.value}
 * @param baseUrl 请求基础url
 * @param sizeOption 可选的分页大小选项
 * @param initSize 初始化页面条数
 * @returns
 */
function usePagination<T>(
  baseUrl: string,
  sizeOption: Array<number> = [5, 7, 9, 11],
  initSize: number = sizeOption[0]
): PaginationResult<T> {
  const loading = ref(false)
  const current = ref(1)
  const total = ref(0)
  const size = ref(initSize)
  const data = ref<T[]>([]) as Ref<T[]>

  function refresh(config?: AxiosRequestConfig) {
    loading.value = true
    request
      .get<Page<T>>(`${baseUrl}/${size.value}/${current.value}`, config)
      .then((res) => {
        const page = res.data
        current.value = page.current
        total.value = page.total
        size.value = page.size
        data.value = page.records
      })
      .catch((err) => {
        console.warn(err)
      })
      .finally(() => {
        loading.value = false
      })
  }

  function onPageChange(pageNumber: number, config?: AxiosRequestConfig) {
    current.value = pageNumber
    refresh(config)
  }

  function onSizeChange(pageSize: number, config?: AxiosRequestConfig) {
    size.value = pageSize
    refresh(config)
  }

  return {
    loading,
    current,
    total,
    size,
    sizeOption,
    data,
    refresh,
    onPageChange,
    onSizeChange
  }
}

export default usePagination
