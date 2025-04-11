import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'

// element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 样式清除
import '@/assets/css/reset.scss'

const app = createApp(App)

// 注册所有的element-plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  if ('development' === import.meta.env.MODE) {
    console.group('global error handler')
    console.log('err', err)
    console.log('vm', vm)
    console.log('info', info)
    console.groupEnd()
  }
}

// 全局警告处理
app.config.warnHandler = (msg, vm, trace) => {
  if ('development' === import.meta.env.MODE) {
    console.group('global warning handler')
    console.log('msg', msg)
    console.log('vm', vm)
    console.log('trace', trace)
    console.groupEnd()
  }
}

// 全局未捕获的promise错误处理
window.addEventListener('unhandledrejection', (event) => {
  event.preventDefault()
  // do noting
})

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})

app.mount('#app')
