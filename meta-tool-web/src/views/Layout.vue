<script lang="ts" setup>
import useSettingStore from '@/stores/setting'
import useUserStore from '@/stores/user'
import {RouterView, useRoute} from 'vue-router'
import {HomeFilled} from '@element-plus/icons-vue'
import Menu from '@/components/layout/Menu.vue'
import Header from '@/components/layout/Header.vue'

const settingStore = useSettingStore()
</script>

<template>
  <el-container>
    <!-- 侧边栏菜单 -->
    <el-aside :class="`aside aside_${settingStore.collapse ? 'collapse' : 'expand'}`">
      <el-scrollbar>
        <div>
          <h1 v-if="!settingStore.collapse">{{ settingStore.title }}</h1>
          <img v-else alt="" src="@/assets/logo.svg" style="width: 63px"/>
        </div>

        <el-menu
            :default-active="useRoute().path"
            active-text-color="#409eff"
            background-color="#304156"
            router
            text-color="#bfcbd9"
            unique-opened
            :collapse="settingStore.collapse"
            :collapse-transition="false"
        >
          <el-menu-item index="/home">
            <el-icon :size="20">
              <HomeFilled/>
            </el-icon>
            <template #title>
              <span>首页</span>
            </template>
          </el-menu-item>
          <Menu :menus="useUserStore().userMenuInfo.menus"></Menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <!-- 头部导航 -->
      <el-header class="header">
        <Header></Header>
      </el-header>
      <el-divider style="margin: 0"></el-divider>
      <!-- 内容区 -->
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
  transition: all 0.5s;
}

.fade-enter-to {
  opacity: 1;
}

.header {
  height: $base_header_height;
}

.aside_expand {
  width: $base_aside_width;
}

.aside_collapse {
  width: $base_aside_collapse_width;
}

.aside {
  height: calc(100vh - 1px);
  background-color: $base_aside_bg_color;

  h1 {
    font-size: 40px;
    color: white;
    padding: 20px 0 20px 0;
    text-align: center;
  }
}

.main {
  /* -1为了防止出现滚动条 */
  height: calc(100vh - $base_header_height - 1px);
  overflow: auto;
}
</style>
