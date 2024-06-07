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
    <el-aside class="aside" width="auto">
      <el-scrollbar>
        <div :style="{ margin: settingStore.collapse ? '0' : '0 20px' }" class="aside_header">
          <img
              :style="{ 'margin-right': settingStore.collapse ? '0' : '10px' }"
              alt=""
              src="@/assets/logo.svg"
          />
          <h1 v-show="!settingStore.collapse">{{ settingStore.title }}</h1>
        </div>

        <!-- 好像只有这样才能改菜单 hover 的颜色 -->
        <el-menu
            :default-active="useRoute().path"
            router
            unique-opened
            :collapse="settingStore.collapse"
            :background-color="settingStore.darkMode ? '#121212' : '#304156'"
            active-text-color="var(--active-color)"
            text-color="var(--text-color)"
            style="border: 0"
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
  border-bottom: 0.8px solid var(--el-border-color);
}

.aside {
  height: calc(100vh - 1px);
  background-color: var(--bg-color);
  border-right: 0.8px solid var(--el-border-color);

  .aside_header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 20px;

    & > img {
      height: 40px;
    }

    & > h1 {
      font-size: 35px;
      color: white;
    }
  }
}

.main {
  /* -1为了防止出现滚动条 */
  height: calc(100vh - $base_header_height - 1px);
  overflow: auto;
}
</style>
