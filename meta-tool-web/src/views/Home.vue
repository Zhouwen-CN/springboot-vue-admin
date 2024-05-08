<script lang="ts" setup>
import {type User} from '@/api/user';
import useRequest from '@/hooks/useRequest';

const {loading, data, refresh} = useRequest<User>({url: '/user'})

</script>

<template>
  <div>
    <h1>主页</h1>
    <button @click="refresh">获取用户</button>
    <div v-if="data">
      <p>loading：{{ loading }}</p>
      <p>用户id：{{ data.id }}</p>
      <p>用户名：{{ data.username }}</p>
      <p>密码：{{ data.password }}</p>
      <p>权限：{{ data.authorities }}</p>
    </div>
    <p style="height:5700px;"></p>
  </div>
</template>

<style lang="scss" scoped></style>