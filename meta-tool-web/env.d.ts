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
    readonly VITE_APP_NAME: string
    readonly VITE_APP_BASE_URL: string
    readonly VITE_APP_SERVER: string
    // 更多环境变量...
}