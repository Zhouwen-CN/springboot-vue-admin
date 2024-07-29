import {defineStore} from 'pinia'
import {ref} from 'vue'

const useSettingStore = defineStore('setting', () => {
    // 项目标题
    const title = ref(import.meta.env.VITE_APP_TITLE)
    // 仓库链接
    const giteeLink = ref(import.meta.env.VITE_APP_GITEE_LINK)
    // 刷新
    const refresh = ref(false)
    // 侧边栏折叠
    const collapse = ref(false)
    // 暗黑模式
    const darkMode = ref(false)

    return {title, giteeLink, refresh, collapse, darkMode}
})

export default useSettingStore
