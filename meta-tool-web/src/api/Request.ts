import type {AxiosRequestConfig, InternalAxiosRequestConfig} from 'axios'
import axios, {AxiosError} from 'axios'
import {ElMessage} from 'element-plus'

// 在等待的请求
const pendingRequest = new Map()

// 如果请求在等待列表，取消请求，并移出等待列表
function removePath(config: InternalAxiosRequestConfig) {
  const path = `${config.method}&${config.url}`
  if (pendingRequest.has(path)) {
    const cancel = pendingRequest.get(path)
    cancel('取消重复请求：' + path)
    pendingRequest.delete(path)
  }
}

// 如果请求不再等待列表，则添加
function addPath(config: InternalAxiosRequestConfig) {
  const path = `${config.method}&${config.url}`
  config.cancelToken =
    config.cancelToken ||
    new axios.CancelToken((cancel) => {
      if (!pendingRequest.has(path)) {
        pendingRequest.set(path, cancel)
      }
    })
}

// 消息提示
function alterMessage(code: number, message: string) {
  if (code > 200 && code < 500) {
    ElMessage.warning(message)
  } else if (code >= 500) {
    ElMessage.error(message)
  }
}

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  timeout: 5000,
  headers: {
    'Content-type': 'application/json;charset=utf-8'
  }
})

// 注册请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    removePath(config)
    addPath(config)
    // 设置请求头 config.headers
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 注册响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    removePath(response.config)
    const result = response.data
    if (result.code > 200) {
      alterMessage(result.code, result.message)
      return Promise.reject(result.message)
    }
    return response
  },
  (error) => {
    if (error instanceof AxiosError) {
      alterMessage(400, error.message)
    } else if (error.response) {
      const response = error.response
      alterMessage(response.status, response.statusText)
    }
    return Promise.reject(error)
  }
)

function request<T = unknown>(url: string, options?: AxiosRequestConfig<T>): Promise<T> {
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

export { pendingRequest, request }
