<script lang="ts" setup>
import useTagViewStore, {type TagView} from '@/stores/tagView'
import {ArrowLeft, ArrowRight} from '@element-plus/icons-vue'
import {ElScrollbar} from 'element-plus'

const router = useRouter()
const route = useRoute()
const tagViewStore = useTagViewStore()

// el 滚动组件
const scrollbarRef = ref<InstanceType<typeof ElScrollbar>>()
// 当前滚动的距离
let currentScrollLeft = 0

// 触发滚动事件时，更新当前滚动的距离
function scrollhandle({scrollLeft}: { scrollLeft: number }) {
  currentScrollLeft = scrollLeft
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

  // 最后剩余可滚动的宽度
  const scrollableDistance = scrollWidth - clientWidth - currentScrollLeft

  // 没有滚动条
  if (clientWidth >= scrollWidth) return
  if (direction === "left") {
    scrollLeft = Math.max(0, currentScrollLeft - step)
  } else {
    scrollLeft = Math.min(currentScrollLeft + step, currentScrollLeft + scrollableDistance)
  }
  scrollbarRef.value!.setScrollLeft(scrollLeft)
}


// 切换 tag view
const changeTagView = (tagView: TagView) => {
  router.push(tagView.path)
}
// 关闭 tag view
const closeTagView = (tagView: TagView) => {
  tagViewStore.removeView(tagView)

  // 如果全部关闭了，则从首页重定向到home
  if (tagView.path === route.path) {
    const visitedViews = tagViewStore.visitedViews
    if (visitedViews.length === 0) {
      router.push("/")
    } else {
      const path = visitedViews[visitedViews.length - 1].path
      router.push(path)
    }
  }
}

// 初始化 tag view，找出所有 affix 的路由
function initTagView() {
  router.getRoutes().forEach((route) => {
    if (route.meta.affix) {

      const tagView: TagView = {
        path: route.path,
        fullPath: route.path,
        name: route.name as string,
        title: route.meta.title as string,
        affix: route.meta?.affix as boolean,
        keepAlive: route.meta?.keepAlive as boolean
      }

      tagViewStore.addView(tagView)
    }
  })
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

    <el-scrollbar ref="scrollbarRef" @scroll="scrollhandle">
      <template v-for="tagView in tagViewStore.visitedViews"
                :key="tagView.title">
        <el-tag
            :class="{ active: route.path === tagView.path }"
            :closable="!tagView.affix"
            class="tag-view-item"
            disable-transitions
            @click="changeTagView(tagView)"
            @close="closeTagView(tagView)">
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
  </div>

</template>

<style lang="scss" scoped>
.scroll-container {
  height: 40px;
  padding: 0 20px;

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
}
</style>
