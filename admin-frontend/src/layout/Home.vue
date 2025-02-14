<script lang="ts" setup>
import useUserStore from '@/stores/user'
import {Link} from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting'

const userStore = useUserStore()
const settingStore = useSettingStore()
</script>

<template>
  <div>
    <el-card>
      <div class="box">
        <img
            alt=""
            class="avatar"
            :src="settingStore.avatarUrl"/>
        <div class="message">
          <h3 class="welcome-msg">Welcome
            {{ userStore.userInfo.username }}
          </h3>
          <div>
            <el-link
                :icon="Link"
                :href="settingStore.giteeLink"
                class="gitee-link"
                target="_blank"
                type="primary">
              {{ settingStore.appName }} Project Gitee Link
            </el-link>
          </div>
        </div>
      </div>
    </el-card>

    <div class="welcome-img">
      <img alt="" src="@/assets/images/welcome.svg" width="400px"/>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.box {
  display: flex;
  justify-content: start;

  .avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }

  .message {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    margin-left: 30px;

    .welcome-msg {
      font-size: 30px;
      font-weight: 700;
    }

    .gitee-link {
      font-size: 16px;
    }
  }
}

.welcome-img {
  display: flex;
  justify-content: center;
  align-items: center;
  /*
  60px: 顶部导航
  40px: main padding
  140px: 卡片
  2px: 不知道是什么
  */
  height: calc(100vh - $base_header_height - 40px - 140px - 2px);
}
</style>
