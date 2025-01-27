const useTagViewStore = defineStore('tagView', () => {
  // todo: 页面缓存，测试阶段
  const cachedViews = ref<string[]>(['dev-swagger'])

  return {cachedViews}
})

export default useTagViewStore
