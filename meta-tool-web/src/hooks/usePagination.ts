import {ref, type Ref} from 'vue'
import request from '@/api/request'
import type {Page} from '@/api/types'
import type {AxiosRequestConfig} from 'axios'

/**
 * 默认走 get 请求，并且不会立即请求，而是返回函数
 * 后端请求接口格式默认一致：${baseUrl}/${size.value}/${current.value}
 * @param baseUrl 请求基础路由
 * @param sizeOption 可选的分页大小选项
 * @returns
 */
function usePagination<T>(baseUrl: string, sizeOption: Array<number> = [5, 7, 9, 11]) {
    const current = ref(1)
    const totle = ref(0)
    const size = ref(sizeOption[0])
    const data = ref<T[]>([]) as Ref<T[]>

    function refresh(config?: AxiosRequestConfig) {
        request
            .get<Page<T>>(`${baseUrl}/${size.value}/${current.value}`, config)
            .then((res) => {
                const page = res.data
                current.value = page.current
                totle.value = page.total
                size.value = page.size
                data.value = page.records
            })
            .catch((err) => {
                console.error(err)
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
        totle,
        size,
        sizeOption,
        data,
        refresh,
        onPageChange,
        onSizeChange
    }
}

export default usePagination
