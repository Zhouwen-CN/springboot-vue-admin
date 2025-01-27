<script lang="ts" setup>
import {type OperationLog, reqGetOperationLogPage} from '@/api/log'

const searchForm = reactive({
  username: '',
  status: undefined,
})

const {
  current,
  total,
  size,
  sizeOption,
  data,
  loading,
  refresh,
  onPageChange,
  onSizeChange
} = reqGetOperationLogPage()

function onSubmit() {
  refresh({
    params: {
      ...searchForm
    }
  })
}

onMounted(() => {
  refresh()
})
</script>

<template>
  <div>
    <el-card>
      <!-- 表单 -->
      <el-form :model="searchForm" inline
               @submit.prevent="onSubmit()">
        <el-form-item label="用户名：">
          <el-input v-model="searchForm.username"
                    clearable
                    placeholder="用户名">
          </el-input>
        </el-form-item>
        <el-form-item label="状态：">
          <el-select
              v-model="searchForm.status"
              clearable
              placeholder="请选择"
              style="width: 120px">
            <el-option :value="1" label="成功"/>
            <el-option :value="0" label="失败"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" native-type="submit"
                     type="primary">
            查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px;">
      <!-- 表格 -->
      <el-table :data="data" border stripe>
        <el-table-column label="用户名称"
                         prop="username"></el-table-column>
        <el-table-column label="操作类型"
                         prop="operation"></el-table-column>
        <el-table-column label="请求地址" prop="url"></el-table-column>
        <el-table-column label="请求方式" prop="method"></el-table-column>
        <el-table-column label="请求参数" prop="params"
                         show-overflow-tooltip></el-table-column>
        <el-table-column label="请求耗时">
          <template #default="{ row } : { row: OperationLog } ">
            {{ row.time }} ms
          </template>
        </el-table-column>
        <el-table-column label="操作状态" prop="status"></el-table-column>
        <el-table-column label="ip地址" prop="ip"></el-table-column>
        <el-table-column label="用户代理" prop="userAgent"
                         show-overflow-tooltip></el-table-column>
        <el-table-column label="创建时间"
                         prop="createTime"></el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination v-model:current-page="current"
                     v-model:page-size="size"
                     :page-sizes="sizeOption" :total="total" background
                     layout="prev, pager, next, ->, total, sizes"
                     style="margin-top: 16px"
                     @current-change="(val) => onPageChange(val, { params: searchForm })"
                     @size-change="(val) => onSizeChange(val, { params: searchForm })"/>
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
