import {type LocationQuery} from 'vue-router'

export interface TagView {
    path: string
    fullPath: string
    name: string
    query?: LocationQuery
    title: string
    affix: boolean
    keepAlive: boolean
    iframe: boolean
    iframeUrl: string
}

export type CloseOption = keyof typeof closeOptionFunction

const closeOptionFunction = {
  selected: (x: number, y: number) => x !== y,
  left: (x: number, y: number) => x >= y,
  right: (x: number, y: number) => x <= y,
  other: (x: number, y: number) => x === y,
  all: () => false
}

const useTagViewStore = defineStore('tagView', () => {
    // 缓存的外链列表
    const cachedIframes = ref<TagView[]>([])
    // 不需要缓存的外链
    const iframe = ref<TagView>()
    // 缓存的组件列表
    const cachedViews = ref<string[]>([])
    // 访问的标签列表
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
        if (view.iframe) {
            if (view.keepAlive) {
                const index = cachedIframes.value.findIndex((item) => item.name === viewName)
                if (index < 0) {
                    cachedIframes.value.push(view)
                }
            } else {
                iframe.value = view
            }
        } else {
            if (view.keepAlive && !cachedViews.value.includes(viewName)) {
                cachedViews.value.push(viewName)
            }
        }
    }

    // 删除页面
    function removeView(_index: number, closeOption: CloseOption, currentRoutePath: string) {
        const remainTagView: TagView[] = []
        const removeTagView: TagView[] = []
        // 是否删除了active tagView
        let isDeletedActive = false

        // 遍历 tags，分别保存需要保留的和需要删除的 tagView
        for (const [i, v] of visitedViews.value.entries()) {
            if (v.affix || closeOptionFunction[closeOption](i, _index)) {
                remainTagView.push(v)
            } else {
                removeTagView.push(v)
            }
        }

        visitedViews.value = remainTagView
        for (const v of removeTagView) {
            if (v.path === currentRoutePath) {
                isDeletedActive = true
            }

            removeCacheView(v)
        }

      return isDeletedActive
  }

    // 删除缓存页面
    function removeCacheView(tagView: TagView) {
        const viewName = tagView.name
        if (tagView.iframe) {
            if (tagView.keepAlive) {
                const index = cachedIframes.value.findIndex((item) => item.name === viewName)
                index > -1 && cachedIframes.value.splice(index, 1)
            } else {
                iframe.value = undefined
            }
        } else {
            if (tagView.keepAlive) {
                const index = cachedViews.value.indexOf(tagView.name)
                index > -1 && cachedViews.value.splice(index, 1)
            }
        }
    }

    // 重置仓库
    function $reset() {
        cachedViews.value = []
        visitedViews.value = []
    }

    return {
        cachedIframes: computed(() => cachedIframes),
        iframe: computed(() => iframe),
        cachedViews: computed(() => cachedViews),
        visitedViews,
        addView,
        removeView,
        removeCacheView,
        $reset
    }
})

export default useTagViewStore
