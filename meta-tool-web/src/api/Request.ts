import axiosInstance from '@/api/AxiosInstance'
import type {AxiosRequestConfig} from 'axios'

export default function request<T = unknown>(
    url: string,
    options?: AxiosRequestConfig<T>
): Promise<T> {
    return new Promise((resolve, reject) => {
        axiosInstance
            .request({
                url,
                ...options
            })
            .then((res) => {
                resolve(res.data)
            })
            .catch((err) => reject(err))
    })
}
