<script lang="ts" setup>
import useSettingStore from '@/stores/setting';
import useUserStore from '@/stores/user';
import {RouterView, useRouter} from 'vue-router'
import {HomeFilled} from '@element-plus/icons-vue'

const userStore = useUserStore()
const settingStore = useSettingStore()
const router = useRouter()

function logout() {
  userStore.$reset()
  router.replace('/login')
}
</script>

<template>
  <el-container>
    <el-aside class="aside" width="200px">
      <el-scrollbar>
        <h1> {{ settingStore.title }} </h1>
        <el-menu active-text-color="#409eff" background-color="#304156" router text-color="#bfcbd9" unique-opened>

          <el-menu-item index="/home">
            <template #title>
              <el-icon :size="24">
                <el-icon>
                  <HomeFilled/>
                </el-icon>
              </el-icon>
              首页
            </template>
          </el-menu-item>

          <template v-for="item in userStore.menus" :key="item.id">

            <el-menu-item v-if="item.children?.length === 0" :index="item.accessPath">
              <template #title>
                <el-icon :size="24">
                  <component :is="item.icon"></component>
                </el-icon>
                {{ item.title }}
              </template>
            </el-menu-item>

            <el-sub-menu v-if="item.children?.length > 0" index="">
              <template #title>
                <el-icon :size="24">
                  <component :is="item.icon"></component>
                </el-icon>
                {{ item.title }}
              </template>
              <el-menu-item v-for="subItem in item.children" :key="subItem.id" :index="subItem.accessPath">
                <template #title>
                  <el-icon :size="24">
                    <component :is="subItem.icon"></component>
                  </el-icon>
                  {{ subItem.title }}
                </template>
              </el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-button @click="logout">退出登入</el-button>
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
