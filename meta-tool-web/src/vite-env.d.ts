/// <reference types="vite/client" />

declare module '*.vue' {
    import { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
  }
  
  /**
   * run dev 默认跟随 --mode development
   * run build 默认跟随 --mode production
   */
  interface ImportMeta {
    readonly env: ImportMetaEnv
  }
  
  // 定义环境变量类型
  interface ImportMetaEnv {
    readonly VITE_APP_NAME: string
    // 更多环境变量...
  }
  