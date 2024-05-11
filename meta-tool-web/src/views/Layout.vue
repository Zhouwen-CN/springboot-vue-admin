<script lang="ts" setup>
import useSettingStore from '@/stores/setting';
import useUserStore from '@/stores/user';
import {RouterView, useRoute} from 'vue-router'
import {HomeFilled} from '@element-plus/icons-vue'
import Menu from '@/components/layout/Menu.vue'
import Header from '@/components/layout/Header.vue'

const settingStore = useSettingStore()
</script>

<template>
  <el-container>
    <el-aside class="aside" width="200px">
      <el-scrollbar>
        <h1> {{ settingStore.title }} </h1>
        <el-menu :default-active="useRoute().path" active-text-color="#409eff" background-color="#304156" router
                 text-color="#bfcbd9" unique-opened>
          <el-menu-item index="/home">
            <template #title>
              <el-space>
                <el-icon :size="24">
                  <HomeFilled/>
                </el-icon>
                <span>首页</span>
              </el-space>
            </template>
          </el-menu-item>

          <Menu :menus="useUserStore().userInfo.menus"></Menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="header">
        <Header></Header>

      </el-header>
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
  transition: all 0.3s;
}

.fade-enter-to {
  opacity: 1;
}

.header {
  // background-color: yellow;
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
