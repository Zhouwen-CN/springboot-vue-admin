<script lang="ts" setup>
import useSettingStore from '@/stores/setting';
import {RouterView} from 'vue-router'

const settingStore = useSettingStore()
</script>

<template>
  <el-container>
    <el-aside class="aside" width="200px">
      <el-scrollbar>
        <h1> {{ settingStore.title }} </h1>
        <el-menu active-text-color="#409eff" background-color="#304156" router text-color="#bfcbd9" unique-opened>
          <el-menu-item index="/home">首页</el-menu-item>
          <el-sub-menu index="/auth">
            <template #title>权限管理</template>
            <el-menu-item index="/auth/user">用户管理</el-menu-item>
            <el-menu-item index="/auth/role">角色管理</el-menu-item>
            <el-menu-item index="/auth/menu">菜单管理</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/field">数据域管理</el-menu-item>
          <el-menu-item index="/range">数据范围管理</el-menu-item>
          <el-menu-item index="/storey">数仓层级管理</el-menu-item>
          <el-menu-item index="/word">词根管理</el-menu-item>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="header">header</el-header>
      <el-main class="main">
        <el-scrollbar>
          <router-view v-slot="{ Component }">
            <transition name="fade">
              <component :is="Component" :key="settingStore.refresh"></component>
            </transition>
          </router-view>
        </el-scrollbar>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
// 动画
.fade-enter-from {
  opacity: 0;
}

.fade-enter-active {
  transition: all 1s;
}

.fade-enter-to {
  opacity: 1;
}

.header {
  background-color: yellow;
  height: $base_header_height;
}

.aside {
  width: $base_aside_width;
  height: 100vh;
  background-color: $base_aside_bg_color;

  h1 {
    font-size: 40px;
    color: white;
    padding: 20px 0 20px 0;
    text-align: center;
  }

}

.main {
  height: calc(100vh - $base_header_height);
  background-color: pink;
  overflow: auto;
}
</style>
