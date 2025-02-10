<script lang="ts" setup>
import useUserStore from '@/stores/user'
import {ArrowDown, ArrowRight, FullScreen} from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting'
import {ElMessage} from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const settingStore = useSettingStore()


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
  userStore.doLogout()
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
</script>

<template>
  <div class="container">
    <div class="left">
      <!-- 展开收起按钮 -->
      <el-icon :size="20" style="margin-right: 10px"
               @click="changeCollapse">
        <component :is="settingStore.collapse ? 'Expand' : 'Fold'">
        </component>
      </el-icon>
      <!-- 左侧面包屑 -->
      <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item v-for="(item, index) in routeInfo"
                            :key="index" :to="item.path">
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
        <el-button :icon="FullScreen" circle size="default"
                   @click="toggleFullScreen"></el-button>
        <el-avatar
            shape="square"
            size="default"
            :src="settingStore.avatarUrl">
        </el-avatar>

        <el-dropdown>
          <span>
            {{ userStore.userInfo.username }}
            <el-icon>
              <ArrowDown/>
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                  @click="logout">退出登入
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-space>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.container {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 0.8px solid var(--el-border-color);

  .left {
    display: flex;
    align-items: center;
  }
}
</style>
