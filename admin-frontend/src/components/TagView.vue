<script lang="ts" setup>
import {ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter()
const route = useRoute()

interface Route {
  path: string
  title: string
}

const tagViewArr = ref<Route[]>([
  {
    path: '/home',
    title: '首页'
  }
])

watch(
    () => route.path,
    () => {
      let routeObj = {
        path: route.path,
        title: route.meta.title as string
      }

      // 404不需要标签
      let isInclude = tagViewArr.value.find((item: Route) => {
        return item.path === routeObj.path || routeObj.path === '/404'
      })

      if (isInclude) return
      tagViewArr.value.push(routeObj)
    },
    {immediate: true}
)

// 切换tag
const changeTag = (path: string) => {
  router.push({path})
}
// 关闭tag
const closeTag = (i: number, path: string) => {
  if (path === route.path) {
    tagViewArr.value.splice(i, 1)
    router.push(tagViewArr.value[tagViewArr.value.length - 1].path)
  } else {
    tagViewArr.value.splice(i, 1)
  }
}
</script>

<template>
  <el-scrollbar>
    <div class="tags">
      <template v-for="(tag, _index) in tagViewArr"
                :key="tag.title">
        <el-tag
            :closable="_index !== 0"
            :effect="route.path === tag.path ? 'dark' : 'light'"
            class="tagItem"
            hit
            @click="changeTag(tag.path)"
            @close="closeTag(_index, tag.path)">
          <div class="tagContent">
            <span v-show="route.path === tag.path" class="dot"></span>
            <span>{{ tag.title }}</span>
          </div>
        </el-tag>
      </template>
    </div>
  </el-scrollbar>
</template>

<style lang="scss" scoped>
.tags {
  margin-top: 5px;
  padding: 0 20px;

  .tagItem {
    margin-right: 10px;

    &:hover {
      cursor: pointer;
    }

    .tagContent {
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
}
</style>
