<script lang="ts" setup>
import {Close} from '@element-plus/icons-vue'
</script>

<template>
  <el-scrollbar>
    <div class="scrollbar">
      <!-- 这里应该是 router-link -->
      <div v-for="item in 50" :key="item" class="scrollbar-item">
        <span style="margin-right: 10px">{{ item }}</span>
        <el-icon class="scrollbar-item-icon" size="12" style="color: #606266">
          <Close/>
        </el-icon>
      </div>
    </div>
  </el-scrollbar>
</template>

<style lang="scss" scoped>
.scrollbar {
  display: flex;

  .scrollbar-item {
    display: flex;
    align-items: center;
    justify-content: space-around;
    height: 30px;
    text-align: center;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    padding: 0 10px;
    margin-right: 10px;

    .scrollbar-item-icon {
      border-radius: 12px;

      &:hover {
        background-color: #dcdfe6;
      }
    }
  }
}
</style>
