<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import useUserStore from '@/stores/user'
import {ArrowDown, ArrowRight, FullScreen, Refresh} from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting';
import {computed, ref} from 'vue';

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const settingStore = useSettingStore()

const editableTabsValue = ref('')

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
  // 删除默认路由以外的路由
  const deleteNames = router.getRoutes().filter(r => !r.meta.require).map(r => (r.name as string))
  deleteNames.forEach(name => router.removeRoute(name))

  userStore.$reset()
  router.replace('/login')
}

// 路由信息
const routeInfo = computed(() => {
  return route.matched.filter(item => item.meta.title)
})
</script>

<template>
  <div class="header">
    <!-- 左侧面包屑 -->
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item v-for="(item, index) in routeInfo" :key="index" :to="item.path">
        <el-icon>
          <component :is="item.meta.icon"></component>
        </el-icon>
        <span style="margin-left: 5px;">{{ item.meta.title }}</span>
      </el-breadcrumb-item>

    </el-breadcrumb>
    <!-- 右侧头像等图标 -->
    <div>
      <el-space size="large">
        <el-button :icon="Refresh" circle size="default" @click="refresh"></el-button>
        <el-button :icon="FullScreen" circle size="default" @click="toggleFullScreen"></el-button>
        <el-avatar shape="square" size="default"
                   src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
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
}
</style>
