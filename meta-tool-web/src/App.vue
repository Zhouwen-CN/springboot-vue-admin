<script setup lang="ts">
import {RouterView} from 'vue-router'
</script>

<template>
  <div class="common-layout">
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
  </div>
</template>

<style lang="scss">
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