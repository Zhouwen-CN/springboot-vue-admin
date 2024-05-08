import {ref} from 'vue'
import request from '@/api/request'

interface UseRequestParams<T> {
    url: string
    method?: 'get' | 'post' | 'put' | 'delete'
    data?: T
    immediate?: boolean
}

function useRequest<T = unknown>(param: UseRequestParams<T>) {
    const loading = ref(false)
    const data = ref<T>()

    param.method ??= 'get'
    const immediate = param.immediate ?? false
    delete param.immediate

    function refresh() {
        loading.value = true
        request
            .request<T>(param)
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
