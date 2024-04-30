import {ref} from 'vue'

export default function useRequest<T = unknown>(request: () => Promise<T>) {
    const loading = ref(true)
    const data = ref<T>()

    function refresh() {
        loading.value = true
        request()
            .then((res) => {
                data.value = res
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
