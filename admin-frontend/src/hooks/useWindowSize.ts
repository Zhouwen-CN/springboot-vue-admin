import useAppStore from '@/stores/app'
const appStpre = useAppStore()

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
    } else {
      appStpre.device = 'desktop'
    }
  }

  onMounted(() => {
    window.addEventListener('resize', handleResize)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', handleResize)
  })
}

export default useWindowSize
