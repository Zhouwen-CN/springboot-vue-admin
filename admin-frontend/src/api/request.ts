import type {AxiosInstance, AxiosRequestConfig} from 'axios'
import axios, {AxiosError} from 'axios'
import {ElMessage} from 'element-plus'
import type {ResultData} from '@/api/types'
import useUserStore from '@/stores/user'
import router from '@/router'
import {deleteAsyncRoutes} from '@/router/asyncRoutes'

const config = {
    baseURL: import.meta.env.VITE_APP_BASE_URL,
    timeout: import.meta.env.VITE_APP_TIMEOUT,
    headers: {
        'Content-type': 'application/json;charset=utf-8'
    }
}

class Request {
    private instance: AxiosInstance

    public constructor(config: AxiosRequestConfig) {
        this.instance = axios.create(config)

        // 请求拦截器
        this.instance.interceptors.request.use(
            (config) => {
                // set token to request header if token not exists
                if (!config.headers.Authorization) {
                    const userStore = useUserStore()
                    const token = userStore.userInfo.accessToken
                    if (token) {
                        config.headers.Authorization = `Bearer ${token}`
                    }
                }
                return config
            },
            (error) => {
                return Promise.reject(error)
            }
        )

        // 响应拦截器
        this.instance.interceptors.response.use(
            async (response) => {
                const {data, config} = response
                // 如果没有权限，并且当前请求不是刷新 token 请求，则执行刷新 token
                if (data.code === 401 && config.url !== '/user/refresh') {
                    const userStore = useUserStore()
                    const isSuccess = await userStore.doRefreshToken()
                    if (isSuccess) {
                        config.headers.Authorization = `Bearer ${userStore.userInfo.accessToken}`
                        return await this.request(config)
                    } else {
                        this.alterMessage(data.code, data.message)
                        return Promise.reject(data.message)
                    }
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

    /**
     *  get 请求
     * @param url 请求地址
     * @param config 请求参数
     * @returns
     */
    public get<T>(url: string, config?: AxiosRequestConfig): Promise<ResultData<T>> {
        return this.instance.get(url, config)
    }

    /**
     *  post(新增) 请求
     * @param url 请求地址
     * @param data 请求体
     * @param config 请求参数
     * @returns
     */
    public post<T, D = unknown>(
        url: string,
        data?: D,
        config?: AxiosRequestConfig
    ): Promise<ResultData<T>> {
        return this.instance.post(url, data, config)
    }

    /**
     * put(更新) 请求
     * @param url 请求地址
     * @param data 请求体
     * @param config 请求参数
     * @returns
     */
    public put<T, D = unknown>(
        url: string,
        data?: D,
        config?: AxiosRequestConfig
    ): Promise<ResultData<T>> {
        return this.instance.put(url, data, config)
    }

    /**
     * delete 请求
     * @param url 请求地址
     * @param config 请求参数
     * @returns
     */
    public delete<T, D>(url: string, config?: AxiosRequestConfig<D>): Promise<ResultData<T>> {
        return this.instance.delete(url, config)
    }

    /**
     * 通用请求
     * @param config 请求配置
     * @returns
     */
    public request<T, D>(config: AxiosRequestConfig<D>): Promise<ResultData<T>> {
        return this.instance.request(config)
    }

    /**
     * 消息提示
     */
    private alterMessage(code: number, message: string): void {
        if (code > 200 && code < 400) {
            ElMessage.info(message)
        } else if (code >= 400 && code < 500) {
            // token失效退出登入
            if (code === 401) {
                useUserStore().$reset()
                deleteAsyncRoutes(router)
            }
            ElMessage.warning(message)
        } else if (code >= 500) {
            ElMessage.error(message)
        }
    }
}

const request = new Request(config)
export default request
