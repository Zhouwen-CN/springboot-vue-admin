<script lang="ts" setup>
import useUserStore from '@/stores/user'
import useSettingStore from '@/stores/setting'

const userStore = useUserStore()
const settingStore = useSettingStore()

</script>

<template>
  <div>
    <el-card>
      <div class="box">
        <img alt="" class="avatar" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
        <div class="message">
          <h3 class="welcome_msg">
            Welcome {{ userStore.userMenuInfo.username }}
          </h3>
          <p class="title_msg">{{ settingStore.title }}</p>
        </div>
      </div>
    </el-card>

    <div class="welcome_img">
      <img alt="" src="@/assets/welcome.svg" width="400px"/>
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
      font-style: italic;
      font-size: 20px;
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
  3px: 不知道为什么差3个像素
  */
  height: calc(100vh - 60px - 40px - 140px - 3px);
}
</style>