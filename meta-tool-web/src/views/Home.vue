<script lang="ts" setup>
import {getUserInfo} from '@/api/User'


const useRequest = () => {
  getUserInfo()
      .then(res => {
        console.log(res);

      })
}




</script>


<template>
  <h1>主页</h1>
  <button @click="useRequest">这是一个按钮</button>
</template>

<style lang="scss" scoped></style>