<script lang="ts" setup>
import {type ErrorLog, reqGetErrorLogPage} from '@/api/log'

const drawer = ref(false)
const errorMsg = ref('')
const {
  current,
  total,
  size,
  sizeOption,
  data,
  refresh,
  onPageChange,
  onSizeChange
} = reqGetErrorLogPage()

function openErrorMessage(row: ErrorLog) {
  drawer.value = true
  errorMsg.value = row.errorMsg
}


onMounted(() => {
  refresh()
})
</script>

<template>
  <el-card>
    <!-- 表格 -->
    <el-table :data="data" border row-key="id" stripe>
      <el-table-column label="用户名称"
                       prop="username"></el-table-column>
      <el-table-column label="请求地址" prop="url"></el-table-column>
      <el-table-column label="请求方式" prop="method"></el-table-column>
      <el-table-column label="请求参数" prop="params"></el-table-column>
      <el-table-column label="ip地址" prop="ip"></el-table-column>
      <el-table-column
          label="用户代理"
          prop="userAgent"
          show-overflow-tooltip></el-table-column>

      <el-table-column label="创建时间"
                       prop="createTime"></el-table-column>
      <el-table-column label="错误信息">
        <template #default="{ row }">
          <el-button type="primary"
                     @click="openErrorMessage(row)">查看异常信息
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination v-model:current-page="current"
                   v-model:page-size="size"
                   :page-sizes="sizeOption" :total="total" background
                   layout="prev, pager, next, ->, total, sizes"
                   style="margin-top: 16px" @current-change="onPageChange"
                   @size-change="onSizeChange"/>

    <!-- 抽屉（关闭时清除错误信息） -->
    <el-drawer v-model="drawer" :with-header="false" size="40%"
               @closed="errorMsg = ''">
      {{ errorMsg }}
    </el-drawer>
  </el-card>
</template>

<style lang="scss" scoped></style>
