const useSettingStore = defineStore('setting', () => {
  // 侧边栏折叠
  const collapse = ref(false)

  return {collapse}
})

export default useSettingStore
