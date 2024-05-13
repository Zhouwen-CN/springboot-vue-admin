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
                <el-icon :size="20">
                  <HomeFilled/>
                </el-icon>
                <span>首页</span>
              </el-space>
            </template>
          </el-menu-item>

          <Menu :menus="useUserStore().userMenuInfo.menus"></Menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="header">
        <Header></Header>
      </el-header>
      <el-divider style="margin: 0;"></el-divider>

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

.aside {
  width: $base_aside_width;
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
