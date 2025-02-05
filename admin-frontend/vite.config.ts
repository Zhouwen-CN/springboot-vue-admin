import {fileURLToPath, URL} from 'node:url'

import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig(({mode}) => {
  const env = loadEnv(mode, process.cwd())
  const regExp = new RegExp(`^${env.VITE_APP_BASE_URL}`)
  return {
    plugins: [
      vue(),
      VueDevTools(),
      // 自动导入，会生成 auto-imports.d.ts 文件
      AutoImport({
        imports: ['vue', 'vue-router', 'pinia'],
        resolvers: [ElementPlusResolver()]
      }),
      // element-ui 自动导入，会生成 components.d.ts 文件
      Components({
        resolvers: [ElementPlusResolver()]
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    css: {
      preprocessorOptions: {
        scss: {
          javascriptEnabled: true,
          additionalData: '@import "./src/assets/css/variables.scss";'
        }
      }
    },
    server: {
      proxy: {
        [env.VITE_APP_BASE_URL]: {
          target: env.VITE_APP_SERVER,
          changeOrigin: true,
          rewrite: (path) => path.replace(regExp, '')
        }
      }
    },
    build: {
      rollupOptions: {
        output: {
          entryFileNames: 'js/[name]-[hash].js',
          chunkFileNames: 'js/[name]-[hash].js',
          assetFileNames(chunkInfo) {
            if (chunkInfo.name?.endsWith('.css')) {
              return 'css/[name]-[hash].css'
            }
            const imagesExt = ['.png', '.jpg', '.jpeg', '.gif', '.svg']
            if (imagesExt.some((ext) => chunkInfo.name?.endsWith(ext))) {
              return 'images/[name]-[hash].[ext]'
            }
            return 'assets/[name]-[hash].[ext]'
          }
          // 虽然小文件会变少，但是可能会影响路由懒加载，暂时先不用
          // manualChunks(id) {
          //   if (id.includes('/node_modules/')) {
          //     if (id.includes('element-plus')) {
          //       return 'element'
          //     }
          //     if (id.includes('swagger-ui-dist')) {
          //       return 'swagger'
          //     }
          //     return 'vendor'
          //   }

          //   return 'index'
          // }
        }
      }
    }
  }
})
