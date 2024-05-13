<script lang="ts" setup>
defineProps(['menus'])
defineOptions({
  name: 'Menu',
})
</script>

<template>
  <template v-for="item in menus" :key="item.id">
    <!-- 没有子菜单 -->
    <el-menu-item v-if="item.children?.length === 0" :index="item.accessPath">
      <template #title>
        <el-space>
          <el-icon :size="20">
            <component :is="item.icon"></component>
          </el-icon>
          <span> {{ item.title }}</span>
        </el-space>
      </template>
    </el-menu-item>

    <!-- 有子菜单，递归 -->
    <el-sub-menu v-if="item.children?.length > 0" :index="item.accessPath">
      <template #title>
        <el-space>
          <el-icon :size="20">
            <component :is="item.icon"></component>
          </el-icon>
          <span> {{ item.title }}</span>
        </el-space>
      </template>
      <Menu :menus="item.children"></Menu>
    </el-sub-menu>
  </template>
</template>

<style lang="scss" scoped></style>
