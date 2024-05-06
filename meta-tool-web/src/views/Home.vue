<script lang="ts" setup>
import {getUserInfo} from '@/api/user'
import useRequest from '@/hooks/useRequest'

const {loading, data, refresh} = useRequest(() => getUserInfo())
</script>

<template>
  <h1>主页</h1>
  <button @click="refresh">这是一个按钮</button>
  <h2>{{ loading }}</h2>
  <h2>{{ data }}</h2>
</template>

<style lang="scss" scoped></style>