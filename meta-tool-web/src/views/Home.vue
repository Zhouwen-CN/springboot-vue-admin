<script lang="ts" setup>
import {reqUser, type User} from '@/api/user';
import {ref} from 'vue';

const user = ref<User>()


function getUser() {
  reqUser().then(res => {
    user.value = res.data
  }).catch(e => {
    console.log(e);
  })
}
</script>

<template>
  <h1>主页</h1>
  <button @click="getUser">获取用户</button>
  <div v-if="user">
    <p>用户id：{{ user.id }}</p>
    <p>用户名：{{ user.username }}</p>
    <p>密码：{{ user.password }}</p>
    <p>权限：{{ user.authorities }}</p>
  </div>
  <p style="height:5700px;"></p>
</template>

<style lang="scss" scoped></style>