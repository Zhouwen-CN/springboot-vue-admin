<script lang="ts" setup>
import useTagViewStore, {type CloseOption, type TagView} from '@/stores/tagView'
import {ArrowLeft, ArrowRight, Back, Close, Minus, Refresh, Right, Sort} from '@element-plus/icons-vue'
import {ElScrollbar} from 'element-plus'

const router = useRouter()
const route = useRoute()
const tagViewStore = useTagViewStore()

// el 滚动组件
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()
// 当前滚动的距离
let currentScrollLeft = 0
// 触发滚动事件时，更新当前滚动的距离
function scrollHandler({scrollLeft}: { scrollLeft: number }) {
  currentScrollLeft = scrollLeft
  closeTagMenu()
}
// 左右滚动
function scrollTo(direction: "left" | "right", step: number = 200) {
  let scrollLeft = 0

  if (!scrollbarRef.value) {
    return
  }

  const scrollWrapRef = scrollbarRef.value.wrapRef
  if (!scrollWrapRef) {
    return
  }

  const scrollWidth = scrollWrapRef.scrollWidth
  const clientWidth = scrollWrapRef.clientWidth

  // 没有滚动条
  if (clientWidth >= scrollWidth) return

  // 最后剩余可滚动的宽度
  const scrollableDistance = scrollWidth - clientWidth - currentScrollLeft

  if (direction === "left") {
    scrollLeft = Math.max(0, currentScrollLeft - step)
  } else {
    scrollLeft = Math.min(currentScrollLeft + step, currentScrollLeft + scrollableDistance)
  }
  scrollbarRef.value!.setScrollLeft(scrollLeft)
}

// 初始化 tagView，找出所有 affix 的路由
function initTagView() {
  router.getRoutes().forEach((route) => {
    if (route.meta.affix) {
      const tagView: TagView = {
        path: route.path,
        fullPath: route.path,
        name: route.name as string,
        query: undefined,
        title: route.meta.title as string,
        affix: route.meta?.affix as boolean,
        keepAlive: route.meta?.keepAlive as boolean
      }

      tagViewStore.addView(tagView)
    }
  })
}

// 切换 tagView
function changeTagView(tagView: TagView) {
  router.push({path: tagView.path, query: tagView.query})
}

// 关闭 tagView
function closeTagView(_index: number, closeOption: CloseOption) {
  const isDeleteActive = tagViewStore.removeView(_index, closeOption, route.path)

  // 是否删除了 active tagView
  if (isDeleteActive) {
    const visitedViews = tagViewStore.visitedViews
    if (visitedViews.length === 0) {
      router.push("/")
    } else {
      const path = visitedViews[visitedViews.length - 1].path
      router.push(path)
    }
  }
}

// 刷新 tagView
function refreshTagView(_index: number) {
  const tagView = tagViewStore.visitedViews[_index]
  tagViewStore.removeCacheView(tagView)
  nextTick(() => {
    router.replace("/redirect" + tagView.path)
  })
}


const tagMenuVisible = ref(false)
const left = ref(0)
const top = ref(0)
const selectedIndex = ref(-1)
// 获取当前组件
const instance = getCurrentInstance()!

// 关闭 tagView 右键菜单
function closeTagMenu() {
  tagMenuVisible.value = false
}

watch(tagMenuVisible, (value) => {
  if (value) {
    document.body.addEventListener("click", closeTagMenu)
  } else {
    document.body.removeEventListener("click", closeTagMenu)
  }
})

// 打开 tagView 右键菜单
function openTagMenu(_index: number, e: MouseEvent) {
  // 右键菜单宽度
  const tagMenuWidth = 100
  // 面包屑的高度
  const headerHeight = 40

  // 当前组件左侧距离窗口左侧的距离
  const offsetLeft = instance.proxy?.$el.getBoundingClientRect().left

  // 当前组件的宽度
  const offsetWidth = instance.proxy?.$el.offsetWidth

  // 最大 left 值
  const maxLeft = offsetWidth - tagMenuWidth

  // 当前组件左侧到鼠标点击的距离
  const l = e.clientX - offsetLeft

  if (l > maxLeft) {
    left.value = maxLeft
  } else {
    left.value = l
  }

  top.value = e.clientY - headerHeight

  tagMenuVisible.value = true
  selectedIndex.value = _index
}

// 是否是固定标签
function isAffix() {
  try {
    return tagViewStore.visitedViews[selectedIndex.value].affix
  } catch (e) {
    return false
  }
}

// 是否是第一个标签
function isFirstTagView() {
  return selectedIndex.value === 0 ||
      selectedIndex.value === tagViewStore.visitedViews.findIndex(v => !v.affix)
}

// 是否是最后一个标签
function isLastTagView() {
  return selectedIndex.value === tagViewStore.visitedViews.length - 1
}

onMounted(() => {
  initTagView()
})
</script>

<template>
  <div class="scroll-container">

    <el-icon class="arrow left" @click="scrollTo('left')">
      <ArrowLeft/>
    </el-icon>

    <el-scrollbar ref="scrollbarRef" @scroll="scrollHandler">
      <template v-for="(tagView, _index) in tagViewStore.visitedViews"
                :key="tagView.title">
        <el-tag
            :class="{ active: route.path === tagView.path }"
            :closable="!tagView.affix"
            class="tag-view-item"
            disable-transitions
            @click="changeTagView(tagView)"
            @close="closeTagView(_index, 'selected')"
            @contextmenu.prevent="openTagMenu(_index, $event)">
          <div class="tag-view-content">
            <span v-show="route.path === tagView.path"
                  class="dot"></span>
            <span>{{ tagView.title }}</span>
          </div>
        </el-tag>
      </template>
    </el-scrollbar>

    <el-icon class="arrow right" @click="scrollTo('right')">
      <ArrowRight/>
    </el-icon>

    <!-- tag标签操作菜单 -->
    <ul v-show="tagMenuVisible"
        :style="{ left: left + 'px', top: top + 'px' }"
        class="tag-menu">
      <li @click="refreshTagView(selectedIndex)">
        <el-icon>
          <Refresh/>
        </el-icon>
        刷新
      </li>
      <li v-if="!isAffix()"
          @click="closeTagView(selectedIndex, 'selected')">
        <el-icon>
          <Close/>
        </el-icon>
        关闭
      </li>
      <li v-if="!isFirstTagView()"
          @click="closeTagView(selectedIndex, 'left')">
        <el-icon>
          <Back/>
        </el-icon>
        关闭左侧
      </li>
      <li v-if="!isLastTagView()"
          @click="closeTagView(selectedIndex, 'right')">
        <el-icon>
          <Right/>
        </el-icon>
        关闭右侧
      </li>
      <li @click="closeTagView(selectedIndex, 'other')">
        <el-icon>
          <Sort/>
        </el-icon>
        关闭其它
      </li>
      <li @click="closeTagView(selectedIndex, 'all')">
        <el-icon>
          <Minus/>
        </el-icon>
        关闭所有
      </li>
    </ul>
  </div>

</template>

<style lang="scss" scoped>
.scroll-container {
  height: 40px;
  padding: 0 20px;
  position: relative;

  display: flex;
  justify-content: space-between;
  align-items: center;

  .arrow {
    font-size: 20px;
    cursor: pointer;

    &.left {
      margin-right: 8px;
    }

    &.right {
      margin-left: 8px;
    }
  }


  .tag-view-item {
    margin-top: 6px;
    margin-right: 8px;
    font-size: 14px;
    height: 28px;
    color: var(--el-text-color-primary);
    background-color: var(--el-bg-color);
    border-color: var(--el-border-color);

    :deep(.el-icon) {
      border-radius: 50%;
      color: var(--el-text-color-primary);

      &:hover {
        background-color: #00000030;
        color: #ffffff;
      }
    }

    &.active {
      background-color: var(--el-color-primary);
      color: #ffffff;
      border-color: var(--el-color-primary);

      :deep(.el-icon) {
        color: #ffffff;
      }
    }

    &:hover {
      cursor: pointer;
    }

    &:last-of-type {
      margin-right: 0;
    }

    .tag-view-content {
      display: flex;
      align-items: center;
      justify-content: space-around;

      .dot {
        width: 6px;
        height: 6px;
        background-color: #fff;
        border-radius: 50%;
        margin-right: 5px;
      }
    }
  }

  .el-scrollbar {
    flex: 1;
    white-space: nowrap;

  }

  .tag-menu {
    position: absolute;
    z-index: 99;
    font-size: 12px;
    background: var(--el-bg-color-overlay);
    border-radius: 4px;
    box-shadow: var(--el-box-shadow-light);

    li {
      padding: 8px 16px;
      cursor: pointer;

      &:hover {
        background: var(--el-fill-color-light);
      }
    }
  }
}
</style>
