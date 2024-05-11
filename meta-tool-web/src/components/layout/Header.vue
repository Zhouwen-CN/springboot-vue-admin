<script lang="ts" setup>
import {useRouter} from 'vue-router'
import useUserStore from '@/stores/user'
import {ArrowDown, ArrowRight, FullScreen, Refresh} from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting';

const router = useRouter()
const userStore = useUserStore()
const settingStore = useSettingStore()

function logout() {
  userStore.$reset()
  router.replace('/login')
}

function refresh() {
  console.log(settingStore.refresh);

  settingStore.refresh = !settingStore.refresh
}
</script>

<template>
  <div class="header">
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>
        权限管理
      </el-breadcrumb-item>
      <el-breadcrumb-item>
        用户管理
      </el-breadcrumb-item>
    </el-breadcrumb>
    <div>
      <el-space size="large">
        <el-button :icon="Refresh" circle size="default" @click="refresh"></el-button>
        <el-button :icon="FullScreen" circle size="default"></el-button>
        <el-avatar shape="square" size="default"
                   src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
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
