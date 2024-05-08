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
          <el-menu-item index="/user">用户</el-menu-item>
          <el-menu-item index="/tree">树形菜单</el-menu-item>
          <el-menu-item index="/transfer">穿梭框</el-menu-item>
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
