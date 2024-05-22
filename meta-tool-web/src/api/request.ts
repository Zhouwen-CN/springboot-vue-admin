import type {AxiosInstance, AxiosRequestConfig, Canceler, InternalAxiosRequestConfig} from 'axios'
import axios, {AxiosError} from 'axios'
import {ElMessage} from 'element-plus'
import type {ResultData} from '@/api/types'
import useUserStore from '@/stores/user'
import router from '@/router'
import {deleteAsyncRoutesAndExit} from '@/router/asyncRoutes'

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
            // set token to request header if exists
            const userStore = useUserStore()
            const token = userStore.userMenuInfo.token
            if (token) {
                config.headers.Authorization = `Bearer ${token}`
            }
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
            if (data.code !== 200) {
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

  public post<T, D = unknown>(
      url: string,
      data?: D,
      config?: AxiosRequestConfig
  ): Promise<ResultData<T>> {
    return this.instance.post(url, data, config)
  }

  public put<T, D = unknown>(
      url: string,
      data?: D,
      config?: AxiosRequestConfig
  ): Promise<ResultData<T>> {
    return this.instance.put(url, data, config)
  }

  public delete<T, D>(url: string, config?: AxiosRequestConfig<D>): Promise<ResultData<T>> {
    return this.instance.delete(url, config)
  }

  public request<T, D>(config: AxiosRequestConfig<D>): Promise<ResultData<T>> {
    return this.instance.request(config)
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

  /**
   * 消息提示
   */
  private alterMessage(code: number, message: string): void {
    if (code > 200 && code < 400) {
      ElMessage.info(message)
    } else if (code >= 400 && code < 500) {
        // token失效退出登入
        if (code === 401) {
            deleteAsyncRoutesAndExit(router, useUserStore())
        }
        ElMessage.warning(message)
    } else if (code >= 500) {
      ElMessage.error(message)
    }
  }
}

const request = new Request(config)
export default request
