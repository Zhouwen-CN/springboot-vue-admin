import {fileURLToPath, URL} from 'node:url'

import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {ElementPlusResolver} from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig(({command, mode}) => {
    const env = loadEnv(mode, process.cwd())
    const regExp = new RegExp(`^${env.VITE_APP_BASE_URL}`)
    return {
        plugins: [
            vue(),
            VueDevTools(),
            AutoImport({
                resolvers: [ElementPlusResolver()]
            }),
            Components({
                resolvers: [ElementPlusResolver()]
            })
        ],
        resolve: {
            alias: {
                '@': fileURLToPath(new URL('./src', import.meta.url))
            }
        },
        css: {},
        server: {
            proxy: {
                [env.VITE_APP_BASE_URL]: {
                    target: env.VITE_APP_SERVER,
                    changeOrigin: true,
                    rewrite: (path) => path.replace(regExp, '')
                }
            }
        }
    }
})
