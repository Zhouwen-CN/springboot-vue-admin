<script lang="ts" setup></script>

<template>
  <el-empty :image-size="300" class="empty" description="No Data"/>
</template>

<style lang="scss" scoped>
.empty {
  width: 100%;
  height: calc(100vh - $base_header_height - 40px);
}
</style>
