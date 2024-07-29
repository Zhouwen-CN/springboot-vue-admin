import {ref, type Ref} from 'vue'
import request from '@/api/request'
import type {Page} from '@/api/types'
import type {AxiosRequestConfig} from 'axios'

export interface PaginationResult<T> {
  current: Ref<number>
  total: Ref<number>
  size: Ref<number>
  sizeOption: Array<number>
  data: Ref<T[]>
  refresh: (config?: AxiosRequestConfig) => void
  onPageChange: (pageNumber: number) => void
  onSizeChange: (pageSize: number) => void
}

/**
 * 走 get 请求，并且不会立即请求
 * 后端请求接口格式保持一致：${baseUrl}/${size.value}/${current.value}
 * @param baseUrl 请求基础url
 * @param sizeOption 可选的分页大小选项
 * @returns
 */
function usePagination<T>(
    baseUrl: string,
    sizeOption: Array<number> = [5, 7, 9, 11]
): PaginationResult<T> {
  const current = ref(1)
  const total = ref(0)
  const size = ref(sizeOption[0])
  const data = ref<T[]>([]) as Ref<T[]>

  function refresh(config?: AxiosRequestConfig) {
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
  }

  function onPageChange(pageNumber: number) {
    current.value = pageNumber
    refresh()
  }

  function onSizeChange(pageSize: number) {
    size.value = pageSize
    refresh()
  }

  return {
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
