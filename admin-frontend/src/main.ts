import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

// element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// cron表达式组件
// @ts-ignore
import noVue3Cron from 'no-vue3-cron'
import 'no-vue3-cron/lib/noVue3Cron.css' // 引入样式
// 样式清除
import './assets/css/reset.scss'
import './assets/css/github-markdown.css'
// 自定义指令，给表单项添加提示
import vTip from './directive/vTip'
import vCopy from './directive/vCopy'

const app = createApp(App)

// 注册所有的element-plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

if ('production' === import.meta.env.MODE) {
  // 全局未捕获的promise错误处理
  window.addEventListener('unhandledrejection', (event) => {
    event.preventDefault()
    // do noting
  })
} else {
  // 全局错误处理
  app.config.errorHandler = (err, vm, info) => {
    console.group('global error handler')
    console.log('err', err)
    console.log('vm', vm)
    console.log('info', info)
    console.groupEnd()
  }
}

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(pinia)
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})
app.use(noVue3Cron)
app.directive('tip', vTip)
app.directive('copy', vCopy)

app.mount('#app')
