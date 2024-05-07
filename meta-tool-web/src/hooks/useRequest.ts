import type {ResultData} from '@/api/types'
import {ref} from 'vue'

export default function useRequest<T = unknown>(request: () => Promise<ResultData<T>>) {
    const loading = ref(true)
    const data = ref<T>()

    function refresh() {
        loading.value = true
        request()
            .then((res) => {
                data.value = res.data
            })
            .catch((error) => {
                console.warn(error)
            })
            .finally(() => {
                loading.value = false
            })
    }

    return {loading, data, refresh}
}
