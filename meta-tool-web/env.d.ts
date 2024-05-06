/// <reference types="vite/client" />

declare module '*.vue' {
    import {DefineComponent} from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}

/**
 * run dev default with --mode development
 * run build default with --mode production
 */
interface ImportMeta {
    readonly env: ImportMetaEnv
}

// 定义环境变量类型
interface ImportMetaEnv {
    // 请求路径前缀
    readonly VITE_APP_BASE_URL: string
    // 请求服务器地址
    readonly VITE_APP_SERVER: string
    // axios超时时间
    readonly VITE_APP_TIMEOUT: number
    // 更多环境变量...
}
