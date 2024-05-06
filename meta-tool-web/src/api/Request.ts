import type {AxiosInstance, AxiosRequestConfig, Canceler, InternalAxiosRequestConfig} from 'axios'
import axios, {AxiosError} from 'axios'
import {ElMessage} from 'element-plus'
import type {ResultData} from '@/api/types'

const config = {
    baseURL: import.meta.env.VITE_APP_BASE_URL,
    timeout: import.meta.env.VITE_APP_TIMEOUT,
    headers: {
        'Content-type': 'application/json;charset=utf-8'
    }
}

class Request {
    private instance: AxiosInstance
    private pendingRequest: Map<string, Canceler>

    public constructor(config: AxiosRequestConfig) {
        this.instance = axios.create(config)
        this.pendingRequest = new Map()

        // 请求拦截器
        this.instance.interceptors.request.use(
            (config) => {
                this.removePath(config)
                this.addPath(config)
                // 设置请求头 config.headers
                return config
            },
            (error) => {
                return Promise.reject(error)
            }
        )

        // 响应拦截器
        this.instance.interceptors.response.use(
            (response) => {
                const {data, config} = response
                this.removePath(config)
                // TODO 应该是 !==200
                if (data.code > 200) {
                    this.alterMessage(data.code, data.message)
                    return Promise.reject(data.message)
                }
                return data
            },
            (error) => {
                if (error instanceof AxiosError) {
                    this.alterMessage(400, error.message)
                } else if (error.response) {
                    const response = error.response
                    this.alterMessage(response.status, response.statusText)
                }
                return Promise.reject(error)
            }
        )
    }

    public getPendingRequest(): Map<string, Canceler> {
        return this.pendingRequest
    }

    public get<T>(url: string, config?: AxiosRequestConfig): Promise<ResultData<T>> {
        return this.instance.get(url, config)
    }

    public post<T>(url: string, config?: AxiosRequestConfig): Promise<ResultData<T>> {
        return this.instance.post(url, config)
    }

    public put<T>(url: string, config?: AxiosRequestConfig): Promise<ResultData<T>> {
        return this.instance.put(url, config)
    }

    public delete<T>(url: string, config?: AxiosRequestConfig): Promise<ResultData<T>> {
        return this.instance.delete(url, config)
    }

    /**
     * 如果请求在等待列表，则取消请求，并从列表删除
     */
    private removePath(config: InternalAxiosRequestConfig): void {
        const path = `${config.method}&${config.url}`
        if (this.pendingRequest.has(path)) {
            const cancel = this.pendingRequest.get(path)
            cancel?.('取消重复请求：' + path)
            this.pendingRequest.delete(path)
        }
    }

    /**
     * 如果不在等待列表，则添加到列表中
     */
    private addPath(config: InternalAxiosRequestConfig): void {
        const path = `${config.method}&${config.url}`
        if (!config.cancelToken) {
            config.cancelToken = new axios.CancelToken((cancel) => {
                if (!this.pendingRequest.has(path)) {
                    this.pendingRequest.set(path, cancel)
                }
            })
        }
    }

    private alterMessage(code: number, message: string): void {
        if (code > 200 && code < 400) {
            ElMessage.info(message)
        } else if (code >= 400 && code < 500) {
            ElMessage.warning(message)
        } else if (code >= 500) {
            ElMessage.error(message)
        }
    }
}

export default new Request(config)
