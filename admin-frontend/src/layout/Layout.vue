<script lang="ts" setup>
import useSettingStore from '@/stores/setting'
import useUserStore from '@/stores/user'
import { HomeFilled } from '@element-plus/icons-vue'
import Menu from '@/layout/components/Menu.vue'
import Header from '@/layout/components/Header.vue'
import TagView from '@/layout/components/TagView.vue'
import useTagViewStore from '@/stores/tagView'
import useWindowSize from '@/hooks/useWindowSize'
import useAppStore from '@/stores/app'

const route = useRoute()
const settingStore = useSettingStore()
const appStore = useAppStore()
const cachedViews = computed(() => useTagViewStore().cachedViews)
useWindowSize()

// 菜单选择事件
function handleMenuSelect() {
  if (appStore.device === 'mobile') {
    settingStore.sidebarHidden = true
  }
}

// 内容区域点击事件
function handleContentClick() {
  if (appStore.device === 'mobile' && !settingStore.sidebarHidden) {
    settingStore.sidebarHidden = true
  }
}
</script>

<template>
  <el-container>
    <!-- 侧边栏菜单 -->
    <el-aside
      class="aside"
      :style="{
        display: appStore.device === 'mobile' && settingStore.sidebarHidden ? 'none' : 'block',
        width: appStore.device === 'mobile' && !settingStore.sidebarHidden ? '180px' : 'auto'
      }"
      @click.stop
    >
      <el-scrollbar>
        <div
          :style="{ margin: settingStore.sidebarCollapse ? '0' : '0 20px' }"
          class="aside-header"
        >
          <img
            :style="{
              'margin-right': settingStore.sidebarCollapse ? '0' : '10px',
              height: appStore.device === 'mobile' ? '26px' : '40px'
            }"
            src="@/assets/logo.svg"
          />
          <h1
            v-show="!settingStore.sidebarCollapse"
            :style="{ fontSize: appStore.device === 'mobile' ? '20px' : '40px' }"
          >
            {{ settingStore.appShortName }}
          </h1>
        </div>

        <el-menu
          :collapse="settingStore.sidebarCollapse"
          :default-active="route.path"
          active-text-color="#409eff"
          background-color="#304156"
          router
          style="border: 0"
          text-color="#ffffff"
          unique-opened
          @select="handleMenuSelect"
        >
          <el-menu-item index="/home">
            <el-icon :size="20">
              <HomeFilled />
            </el-icon>
            <template #title>
              <span>首页</span>
            </template>
          </el-menu-item>
          <Menu :menus="useUserStore().menuInfo"></Menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container @click="handleContentClick">
      <!-- 头部导航 -->
      <el-header class="header">
        <div>
          <Header></Header>
          <TagView></TagView>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="main">
        <el-scrollbar>
          <router-view>
            <template #default="{ Component, route }">
              <transition name="fade">
                <keep-alive :include="cachedViews" :max="5">
                  <component :is="Component" :key="route.path" />
                </keep-alive>
              </transition>
            </template>
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
  padding: 0;
  border-bottom: 0.8px solid var(--el-border-color);
}

.aside {
  height: 100vh;
  background-color: #304156;
  border-right: 0.8px solid var(--el-border-color);

  .aside-header {
    height: $base_header_height;
    display: flex;
    align-items: center;
    justify-content: center;

    & > h1 {
      color: white;
    }
  }
}

.main {
  height: calc(100vh - $base_header_height);
  overflow: auto;
}
</style>
