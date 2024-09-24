import {defineStore} from 'pinia'
import {ref} from 'vue'

const useSettingStore = defineStore('setting', () => {
    // 刷新
    const refresh = ref(false)
    // 侧边栏折叠
    const collapse = ref(false)
    // 暗黑模式
    const darkMode = ref(false)

    return {refresh, collapse, darkMode}
})

export default useSettingStore
