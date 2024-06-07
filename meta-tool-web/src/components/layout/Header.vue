<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import useUserStore from '@/stores/user'
import {ArrowDown, ArrowRight, FullScreen, Moon, Refresh, Sunny} from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting'
import {computed} from 'vue'
import {deleteAsyncRoutesAndExit} from '@/router/asyncRoutes'
import {ElMessage} from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const settingStore = useSettingStore()

// 刷新
function refresh() {
  settingStore.refresh = !settingStore.refresh
}

// 切换全屏模式
function toggleFullScreen() {
  const isFullScreen = document.fullscreenElement

  if (!isFullScreen) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 登出
function logout() {
  deleteAsyncRoutesAndExit(router, userStore)
  settingStore.darkMode = false
  document.documentElement.className = 'light'
  ElMessage.success('退出成功')
}

// 路由信息
const routeInfo = computed(() => {
  return route.matched.filter((item) => item.meta.title)
})

// 菜单收起/展开
function changeCollapse() {
  settingStore.collapse = !settingStore.collapse
}

// 暗黑模式
function changeTheme() {
  const html = document.documentElement
  settingStore.darkMode ? (html.className = 'dark') : (html.className = 'light')
}
</script>

<template>
  <div class="header">
    <div class="left">
      <!-- 展开收起按钮 -->
      <el-icon :size="20" style="margin-right: 10px" @click="changeCollapse">
        <component :is="settingStore.collapse ? 'Expand' : 'Fold'"></component>
      </el-icon>
      <!-- 左侧面包屑 -->
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item v-for="(item, index) in routeInfo" :key="index" :to="item.path">
          <el-icon>
            <component :is="item.meta.icon"></component>
          </el-icon>
          <span style="margin-left: 5px">{{ item.meta.title }}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <!-- 右侧头像等图标 -->
    <div>
      <el-space size="large">
        <el-switch
            v-model="settingStore.darkMode"
            :active-action-icon="Moon"
            :inactive-action-icon="Sunny"
            size="large"
            @change="changeTheme"
        />
        <el-button :icon="Refresh" circle size="default" @click="refresh"></el-button>
        <el-button :icon="FullScreen" circle size="default" @click="toggleFullScreen"></el-button>
        <el-avatar
            shape="square"
            size="default"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        >
        </el-avatar>

        <el-dropdown>
          <span>
            {{ userStore.userMenuInfo.username }}
            <el-icon>
              <ArrowDown/>
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="logout">退出登入</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-space>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .left {
    display: flex;
    align-items: center;
  }
}
</style>
