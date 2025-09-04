<script lang="ts" setup>
import { type ErrorLogVo, reqGetErrorLogPage } from '@/api/log'

// 抽屉可见性
const drawerVisible = ref(false)
// 查询用户名
const searchName = ref('')
// 错误信息
const errorMsg = ref('')

// 分页
const { current, total, size, sizeOption, data, loading, refresh, onPageChange, onSizeChange } =
  reqGetErrorLogPage()

// 打开抽屉
function openErrorMessage(row: ErrorLogVo) {
  drawerVisible.value = true
  errorMsg.value = row.errorMsg
}

// 表单提交
function onSubmit() {
  refresh({
    params: {
      username: searchName.value
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
      <el-form inline @submit.prevent="onSubmit()" label-width="auto">
        <el-form-item label="用户名">
          <el-input v-model="searchName" clearable placeholder="用户名"> </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" native-type="submit" type="primary"> 查询 </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格 -->
      <el-table :border="true" :data="data" show-overflow-tooltip>
        <el-table-column label="用户名称" prop="createUser"></el-table-column>
        <el-table-column label="请求地址" prop="url"></el-table-column>
        <el-table-column label="请求方式" prop="method"></el-table-column>
        <el-table-column label="请求参数" prop="params"></el-table-column>
        <el-table-column label="ip地址" prop="ip"></el-table-column>
        <el-table-column label="用户代理" prop="userAgent"></el-table-column>

        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="错误信息">
          <template #default="{ row }: { row: ErrorLogVo }">
            <el-button-group class="ml-4">
              <el-button icon="View" type="primary" @click="openErrorMessage(row)" />
              <el-button icon="DocumentCopy" type="primary" v-copy="row.errorMsg" />
            </el-button-group>
          </template>
        </el-table-column>
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
        @current-change="(val: number) => onPageChange(val)"
        @size-change="(val: number) => onSizeChange(val)"
      />

      <!-- 抽屉（关闭时清除错误信息） -->
      <el-drawer v-model="drawerVisible" :with-header="false" size="50%" @closed="errorMsg = ''">
        <div style="white-space: pre-wrap">{{ errorMsg }}</div>
      </el-drawer>
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
