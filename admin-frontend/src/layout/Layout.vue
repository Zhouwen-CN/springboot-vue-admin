<script lang="ts" setup>
import useSettingStore from '@/stores/setting'
import useUserStore from '@/stores/user'
import {HomeFilled} from '@element-plus/icons-vue'
import Menu from '@/layout/components/Menu.vue'
import Header from '@/layout/components/Header.vue'
import TagView from '@/layout/components/TagView.vue'

const route = useRoute()
const settingStore = useSettingStore()
</script>

<template>
  <el-container>
    <!-- 侧边栏菜单 -->
    <el-aside class="aside" width="auto">
      <el-scrollbar>
        <div
            :style="{ margin: settingStore.collapse ? '0' : '0 20px' }"
            class="aside_header">
          <img
              :style="{ 'margin-right': settingStore.collapse ? '0' : '10px' }"
              alt=""
              src="@/assets/logo.svg"/>
          <h1 v-show="!settingStore.collapse">SV Admin</h1>
        </div>

        <el-menu
            :collapse="settingStore.collapse"
            :default-active="route.path"
            active-text-color="#409eff"
            background-color="#304156"
            router
            style="border: 0"
            text-color="#ffffff"
            unique-opened>
          <el-menu-item index="/home">
            <el-icon :size="20">
              <HomeFilled/>
            </el-icon>
            <template #title>
              <span>首页</span>
            </template>
          </el-menu-item>
          <Menu :menus="useUserStore().menuInfo"></Menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
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
          <router-view v-slot="{ Component }">
            <transition name="fade">
              <component :is="Component" :key="settingStore.refresh">
              </component>
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
  padding: 0;
  border-bottom: 0.8px solid #F6F6F6;
}

.aside {
  height: calc(100vh - 1px);
  background-color: #304156;
  border-right: 0.8px solid #F6F6F6;

  .aside_header {
    height: $base_header_height;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 20px;

    & > img {
      height: 40px;
    }

    & > h1 {
      font-size: 40px;
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
