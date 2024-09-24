<script lang="ts" setup>
import {ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter()
const route = useRoute()

interface routeObj {
  path: string
  title: string
}

const tagViewArr = ref(<routeObj[]>[
  {
    path: '/home',
    title: '首页'
  }
])

watch(
    () => route.path,
    (_) => {
      let routeObj = <routeObj>{
        path: route.path,
        title: route.meta.title as string
      }

      // 404不需要标签
      let isInclude = tagViewArr.value.find((item: routeObj) => {
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
      <template v-for="(item, i) in tagViewArr" :key="item.title">
        <el-tag
            :closable="tagViewArr.length !== 1 && i !== 0"
            :effect="route.path === item.path ? 'dark' : 'light'"
            class="tagItem"
            hit
            @click="changeTag(item.path)"
            @close="closeTag(i, item.path)"
        >
          <div class="tagContent">
            <div v-show="route.path === item.path" class="dot"></div>
            <text>{{ item.title }}</text>
          </div>
        </el-tag>
      </template>
    </div>
  </el-scrollbar>
</template>

<style lang="scss" scoped>
.tags {
  display: flex;
  align-items: center;
  margin-top: 5px;
  padding: 0 20px;

  &:hover {
    cursor: pointer;
  }

  .tagItem {
    margin-right: 5px;

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
