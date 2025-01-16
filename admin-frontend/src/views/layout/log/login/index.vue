<script lang="ts" setup>
const searchForm = reactive({
  username: '',
  operation: undefined,
  status: undefined
})
</script>

<template>
  <div>
    <el-card>
      <el-form :model="searchForm" inline>
        <el-form-item label="用户名：">
          <el-input v-model="searchForm.username"
                    clearable
                    placeholder="用户名">
          </el-input>
        </el-form-item>
        <el-form-item label="操作类型：">
          <el-select
              v-model="searchForm.operation"
              clearable
              placeholder="请选择"
              style="width: 120px">
            <el-option :value="1" label="登入"/>
            <el-option :value="0" label="登出"/>
          </el-select>
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
          <el-button type="primary"> 查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px;">
      <el-table border stripe>
        <el-table-column label="用户名称"
                         prop="username"></el-table-column>
        <el-table-column label="操作类型"
                         prop="operation"></el-table-column>
        <el-table-column label="操作状态" prop="status"></el-table-column>
        <el-table-column label="ip地址" prop="ip"></el-table-column>
        <el-table-column label="用户代理"
                         prop="userAgent"></el-table-column>
        <el-table-column label="创建时间"
                         prop="createTime"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style lang="scss" scoped></style>
