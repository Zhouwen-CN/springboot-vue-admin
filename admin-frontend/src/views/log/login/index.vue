<script lang="ts" setup>
import { reqGetLoginLogPage } from '@/api/log'

// 表单数据
const searchForm = reactive({
  username: '',
  operation: undefined,
  status: undefined
})

// 分页
const { current, total, size, sizeOption, data, loading, refresh, onPageChange, onSizeChange } =
  reqGetLoginLogPage()

// 表单查询
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
      <el-form :model="searchForm" inline @submit.prevent="onSubmit()" label-width="auto">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" clearable placeholder="用户名"> </el-input>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select
            v-model="searchForm.operation"
            clearable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option :value="1" label="登入" />
            <el-option :value="0" label="退出" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            clearable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option :value="1" label="成功" />
            <el-option :value="0" label="失败" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" native-type="submit" type="primary"> 查询 </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格 -->
      <el-table :border="true" :data="data" show-overflow-tooltip>
        <el-table-column label="用户名称" prop="username"></el-table-column>
        <el-table-column label="操作类型" prop="operation"></el-table-column>
        <el-table-column label="操作状态" min-width="40px" prop="status"></el-table-column>
        <el-table-column label="ip地址" prop="ip"></el-table-column>
        <el-table-column label="用户代理" prop="userAgent"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="current"
        v-model:page-size="size"
        :page-sizes="sizeOption"
        :total="total"
        background
        layout="prev, pager, next, jumper, ->, total, sizes"
        style="margin-top: 16px"
        @current-change="(val: number) => onPageChange(val, { params: searchForm })"
        @size-change="(val: number) => onSizeChange(val, { params: searchForm })"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
