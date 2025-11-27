import useAppStore from '@/stores/app'
import useSettingStore from '@/stores/setting'
const appStpre = useAppStore()
const settingStore = useSettingStore()

/**
 * 保存视口宽高，并判断设备类型
 */
function useWindowSize() {
  const MOBILE_MAX_WIDTH = 992 // refer to Bootstrap's responsive design
  const handleResize = () => {
    const width = window.innerWidth
    appStpre.windowWidth = width
    appStpre.windowHeight = window.innerHeight
    if (width <= MOBILE_MAX_WIDTH) {
      appStpre.device = 'mobile'
      settingStore.collapse = false
      settingStore.hidden = true
    } else {
      appStpre.device = 'desktop'
      settingStore.collapse = false
      settingStore.hidden = false
    }
  }

  onMounted(() => {
    handleResize()
    window.addEventListener('resize', handleResize)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
  })
}

export default useWindowSize
