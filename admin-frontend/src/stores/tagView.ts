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

  // 删除页面
  function removeView(view: TagView) {
    removeVisitedView(view)
    removeCachedView(view)
  }

  // 删除访问页面
  function removeVisitedView(view: TagView) {
    for (const [i, v] of visitedViews.value.entries()) {
      if (v.path === view.path) {
        visitedViews.value.splice(i, 1)
        break
      }
    }
  }

  // 删除缓存页面
  function removeCachedView(view: TagView) {
    const index = cachedViews.value.indexOf(view.name)
    index > -1 && cachedViews.value.splice(index, 1)
  }

  return {cachedViews, visitedViews, addView, removeView}
})

export default useTagViewStore
