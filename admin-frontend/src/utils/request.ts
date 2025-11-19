import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import type { ResultData } from '@/utils/requestTypes'
import useUserStore from '@/stores/user'
import router from '@/router'
import { deleteAsyncRoutes } from '@/router/asyncRoutes'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from '@/stores/setting'

class Request {
  private instance: AxiosInstance

  public constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config)

    // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        // 设置请求头，如果token不存在的话
        if (!config.headers.authorization) {
          const token = useUserStore().userInfo.token
          if (token) {
            config.headers.authorization = `Bearer ${token}`
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
        const userStore = useUserStore()

        // 如有响应头中有新的token，写入状态
        const refreshedToken = response.headers.authorization
        if (refreshedToken) {
          userStore.userInfo.token = refreshedToken
        }

        const { data, config } = response
        // 如果没有权限，并且当前请求不是刷新 token 请求，则执行刷新 token
        if (data?.code === 401 && config.url !== '/user/refresh') {
          const isSuccess = await userStore.doRefreshToken()
          if (isSuccess) {
            config.headers.Authorization = `Bearer ${userStore.userInfo.token}`
            return await this.request(config)
          }
        }

        // 请求错误处理
        // tips：/v3/api-docs是框架的接口，没有R对象的封装，就没有 code 属性
        if (data.code && data.code !== 200) {
          this.alterMessage(data.code, data.message)
          return Promise.reject(data.message)
        }

        // 如果是文件下载，直接返回response，因为需要从请求头中获取文件名
        if (response.headers['content-disposition']) {
          return response
        }
        return data
      },
      (error) => {
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
  public post<T = unknown, D = unknown>(
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
  public put<T = unknown, D = unknown>(
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
  public delete<T = unknown, D = unknown>(
    url: string,
    config?: AxiosRequestConfig<D>
  ): Promise<ResultData<T>> {
    return this.instance.delete(url, config)
  }

  public patch<T = unknown, D = unknown>(
    url: string,
    data?: D,
    config?: AxiosRequestConfig
  ): Promise<ResultData<T>> {
    return this.instance.patch(url, data, config)
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
   * 文件下载，默认get，可以通过请求配置修改
   * @param url 请求地址
   * @param config 请求配置
   * @returns
   */
  public download(url: string, config?: AxiosRequestConfig): Promise<AxiosResponse<Blob>> {
    return this.instance.request({
      url,
      method: 'get',
      responseType: 'blob',
      ...config
    })
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
        useTagViewStore().$reset()
        useSettingStore().$reset()
        deleteAsyncRoutes(router)
      }
      ElMessage.warning(message)
    } else if (code >= 500) {
      ElMessage.error(message)
    }
  }
}

export const baseConfig = {
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  timeout: import.meta.env.VITE_APP_TIMEOUT,
  headers: {
    'Content-type': 'application/json;charset=utf-8'
  }
}

const request = new Request(baseConfig)
export default request
