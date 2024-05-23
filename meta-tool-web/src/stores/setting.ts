import {defineStore} from 'pinia'
import {ref} from 'vue'

const useSettingStore = defineStore('setting', () => {
    const title = ref(import.meta.env.VITE_APP_TITLE)
    const giteeLink = ref(import.meta.env.VITE_APP_GITEE_LINK)
    const refresh = ref(false)
    const collapse = ref(false)
    const darkMode = ref(false)
    return {title, giteeLink, refresh, collapse, darkMode}
})

export default useSettingStore
