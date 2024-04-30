<script lang="ts" setup>

import {RouterView} from "vue-router";
</script>

<template>
  <el-container>
    <el-header class="header">
    </el-header>
    <el-container>
      <el-aside class="aside" width="200px">
      </el-aside>
      <el-main class="main">
        <RouterView/>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.header {
  background-color: yellow;
}

.aside {
  background-color: green;
}

.main {
  background-color: pink;
}
</style>