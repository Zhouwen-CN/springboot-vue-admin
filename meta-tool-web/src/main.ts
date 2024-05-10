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

// 全局异常处理
app.config.errorHandler = (err, instance, info) => {
    console.error(
        '😂Component route:',
        instance?.$route,
        '\n',
        'Error message:',
        err,
        '\n',
        'From:',
        info
    )
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
    locale: zhCn
})

app.mount('#app')
