import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'

// element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// @ts-ignore
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

const app = createApp(App)

// global error handler
app.config.errorHandler = (err, instance, info) => {
    console.error('😂😂😂Component route:', instance?.$route, '\n',
        'Error message:', err, '\n',
        'From:', info);
};

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
    locale: zhCn
})

app.mount('#app')
