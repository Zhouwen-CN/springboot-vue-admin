export interface TagView {
  path: string
  fullPath: string
  name: string
  title: string
  affix: boolean
  keepAlive: boolean
}

const useTagViewStore = defineStore('tagView', () => {
  const cachedViews = ref<string[]>([])
  const visitedViews = ref<TagView[]>([])

  // 添加页面
  function addView(view: TagView) {
    addVisitedView(view)
    addCachedView(view)
  }

  // 添加访问页面
  function addVisitedView(view: TagView) {
    if (visitedViews.value.some((v) => v.path === view.path)) {
      return
    }

    if (view.affix) {
      visitedViews.value.unshift(view)
    } else {
      visitedViews.value.push(view)
    }
  }

  // 添加缓存页面
  function addCachedView(view: TagView) {
    const viewName = view.name
    if (view.keepAlive && !cachedViews.value.includes(viewName)) {
      cachedViews.value.push(viewName)
    }
  }

  // 删除页面（不应该删除缓存，删除缓存应该在刷新页面的时候）
  function removeView(view: TagView) {
    const index = visitedViews.value.findIndex((v) => v.path === view.path)
    if (index !== -1) {
      visitedViews.value.splice(index, 1)
    }
  }

  return {cachedViews, visitedViews, addView, removeView}
})

export default useTagViewStore
