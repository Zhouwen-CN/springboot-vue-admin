<script lang="ts" setup>
import useUserStore from '@/stores/user'
const userStore = useUserStore()

const model = defineModel<number>({
  required: true
})

// 树形选择器数据
const data = computed(() => {
  return [
    {
      id: 0,
      title: '主类目',
      accessPath: '',
      icon: '',
      keepAlive: false,
      pid: 0,
      children: userStore.menuInfo.filter((menu) => menu.title !== '首页')
    }
  ]
})
</script>

<template>
  <el-tree-select
    v-model="model"
    node-key="id"
    :props="{ value: 'id', label: 'title', children: 'children' }"
    :data="data"
    :default-expanded-keys="[0]"
    check-strictly
    filterable
  />
</template>

<style lang="scss" scoped></style>
