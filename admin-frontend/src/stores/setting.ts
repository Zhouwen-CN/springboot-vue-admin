const useSettingStore = defineStore('setting', () => {
  // 刷新
  const refresh = ref(false)
  // 侧边栏折叠
  const collapse = ref(false)

  return {refresh, collapse}
})

export default useSettingStore
