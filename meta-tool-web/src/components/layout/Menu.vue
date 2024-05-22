<script lang="ts" setup>
import useSettingStore from '@/stores/setting'

const settingStore = useSettingStore()
defineProps(['menus'])
defineOptions({
  name: 'Menu'
})
</script>

<template>
  <template v-for="item in menus" :key="item.id">
    <!-- 没有子菜单 -->
    <el-menu-item v-if="item.children?.length === 0" :index="item.accessPath">
      <el-icon :size="20">
        <component :is="item.icon"></component>
      </el-icon>
      <template #title>
        <span> {{ item.title }} </span>
      </template>
    </el-menu-item>

    <!-- 有子菜单，递归 -->
    <el-sub-menu v-if="item.children?.length > 0" :index="item.accessPath">
      <template #title>
        <el-icon :size="20">
          <component :is="item.icon"></component>
        </el-icon>
        <span> {{ item.title }}</span>
      </template>
      <Menu :menus="item.children"></Menu>
    </el-sub-menu>
  </template>
</template>

<style lang="scss" scoped></style>
