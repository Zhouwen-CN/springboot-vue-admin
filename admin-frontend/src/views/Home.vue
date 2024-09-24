<script lang="ts" setup>
import useUserStore from '@/stores/user'
import useSettingStore from '@/stores/setting'
import {Link} from '@element-plus/icons-vue'

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
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
        <div class="message">
          <h3 class="welcome_msg">Welcome
            {{ userStore.userInfo.username }}</h3>
          <div>
            <el-link
                href="https://gitee.com/Zhouwen-CN/springboot-vue-admin"
                :icon="Link"
                class="title_msg"
                target="_blank"
                type="primary">
              Springboot-Vue-Admin Project Gitee Link
            </el-link>
          </div>
        </div>
      </div>
    </el-card>

    <div class="welcome_img">
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

    .welcome_msg {
      font-size: 30px;
      font-weight: 700;
    }

    .title_msg {
      font-size: 16px;
    }
  }
}

.welcome_img {
  display: flex;
  justify-content: center;
  align-items: center;
  /*
  60px: 顶部导航
  40px: main padding
  140px: 卡片
  3px: 不知道为什么差3个像素，可能是border
  */
  height: calc(100vh - $base_header_height - 40px - 140px - 3px);
}
</style>
