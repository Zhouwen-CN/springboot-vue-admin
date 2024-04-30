import axios from 'axios'
import {ElMessage} from 'element-plus'

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
        // const existingRequest = axiosInstance.pendingRequests.find(
        //   req => req.url === config.url
        // );

        // if (existingRequest) {
        //   existingRequest.abort(); // 取消之前的请求
        // }

        // axiosInstance.pendingRequests.push(
        //   axios.CancelToken.source().cancel(() => {
        //     // 当请求取消时执行的操作
        //   })
        // );
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
        // if (response.status !== 200) {
        //   alterMessage(result.code, result.message)
        //   return Promise.reject(result.message)
        // }
        return response
    },
    (error) => {
        if (error.response) {
            const response = error.response
            alterMessage(response.status, response.statusText)
        }
        return Promise.reject(error)
    }
)

export default axiosInstance
