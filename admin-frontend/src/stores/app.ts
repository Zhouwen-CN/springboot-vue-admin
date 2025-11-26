const useAppStore = defineStore('PINIA:APP-STORE', () => {
  // 视口宽高
  const windowWidth = ref(window.innerWidth)
  const windowHeight = ref(window.innerHeight)
  // 设备
  const device = ref<'desktop' | 'mobile'>('desktop')

  return {
    device,
    windowWidth,
    windowHeight
  }
})

export default useAppStore
