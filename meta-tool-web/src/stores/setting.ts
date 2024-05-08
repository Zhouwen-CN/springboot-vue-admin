import {defineStore} from 'pinia'
import {ref} from 'vue'

const useSettingStore = defineStore('setting', () => {
  const title = ref(import.meta.env.VITE_APP_TITLE)
  const refresh = ref(false)
  return {title, refresh}
})

export default useSettingStore
