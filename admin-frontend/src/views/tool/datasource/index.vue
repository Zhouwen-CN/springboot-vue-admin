<script lang="ts" setup>
import {
  type DataSourceForm,
  type DataSourceVo,
  reqCheckConnection,
  reqGetDataSourcePage,
  reqRemoveDataSourceById,
  reqRemoveDataSourceByIds,
  reqSaveDataSource
} from '@/api/tool/datasource'
import { ref } from 'vue'
import { CircleCheck, Delete, Edit, Plus, Search } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

// 分页
const {
  loading: pageLoading,
  current,
  total,
  size,
  sizeOption,
  data: pageData,
  refresh,
  onPageChange,
  onSizeChange
} = reqGetDataSourcePage()

// 查询数据源配置
const searchName = ref('')
function searchByName() {
  searchName.value = searchName.value.trim()
  refresh({ params: { searchName: searchName.value } })
}

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 添加数据源配置
function addDataSource() {
  toggleDialog.show = true
  toggleDialog.title = '添加数据源配置'
}

// 更新数据源配置
function updateDataSource(row: DataSourceVo) {
  toggleDialog.show = true
  toggleDialog.title = '修改数据源配置'
  dataSourceForm.id = row.id
  dataSourceForm.name = row.name
  dataSourceForm.url = row.url
  dataSourceForm.username = row.username
}

// 表单提交
const saveLoading = ref(false)
const dataSourceForm = reactive<DataSourceForm>({
  id: undefined,
  name: '',
  url: '',
  username: '',
  password: ''
})
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    // 选中的 和 半选中的菜单
    await reqSaveDataSource(dataSourceForm)
    refresh({ params: { searchName: searchName.value } })
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 删除数据源配置
async function deleteDataSource(id: number) {
  await reqRemoveDataSourceById(id)
  refresh({ params: { searchName: searchName.value } })
  ElMessage.success('操作成功')
}

// 批量删除数据源配置
const deleteIds = ref<number[]>([])
function handleSelectionChange(dataSourceList: DataSourceVo[]) {
  deleteIds.value = dataSourceList.map((dataSource) => dataSource.id)
}
async function deleteDataSources() {
  await reqRemoveDataSourceByIds(deleteIds.value)
  refresh({ params: { searchName: searchName.value } })
  ElMessage.success('操作成功')
}

// 表单校验
const ruleFormRef = ref<FormInstance>()
const validateUrl = (rule: any, value: any, callback: any) => {
  if (value && value.startsWith('jdbc:')) {
    callback()
  } else {
    callback(new Error('url 须以 jdbc: 开头'))
  }
}
const rules = reactive<FormRules<typeof dataSourceForm>>({
  name: [
    { required: true, message: '请输入数据源配置名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  url: [{ validator: validateUrl, trigger: 'blur' }],
  username: [
    { required: true, message: '请输入数据源配置用户名', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入数据源配置密码', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ]
})

// 对话框关闭时清空数据 和 错误提示样式
function clean() {
  toggleDialog.title = ''
  dataSourceForm.id = undefined
  dataSourceForm.name = ''
  dataSourceForm.url = ''
  dataSourceForm.username = ''
  dataSourceForm.password = ''
  ruleFormRef.value?.clearValidate()
}

// 检查连接
async function checkConnection(id: number) {
  await reqCheckConnection(id)
  ElMessage.success('连接成功')
}

// 挂载时刷新
onMounted(() => {
  refresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="searchByName()">
        <el-form-item label="关键字">
          <el-input v-model="searchName" clearable placeholder="关键字"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" :loading="pageLoading" native-type="submit" type="primary"
            >搜索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格上面的按钮 -->
      <div>
        <el-button :icon="Plus" type="primary" @click="addDataSource">新建</el-button>
        <el-popconfirm title="是否删除？" @confirm="deleteDataSources">
          <template #reference>
            <el-button :disabled="deleteIds.length == 0" :icon="Delete" type="danger"
              >批量删除
            </el-button>
          </template>
        </el-popconfirm>
      </div>

      <!-- 表格 -->
      <el-table
        :border="true"
        :data="pageData"
        show-overflow-tooltip
        style="margin-top: 16px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="45px" />
        <el-table-column label="ID" prop="id"></el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="url" prop="url"></el-table-column>
        <el-table-column label="用户名" prop="username"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作" min-width="90px">
          <template #default="{ row }: { row: DataSourceVo }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="updateDataSource(row)"></el-button>
              <el-button
                :icon="CircleCheck"
                type="warning"
                @click="checkConnection(row.id)"
              ></el-button>
              <el-popconfirm title="是否删除？" @confirm="deleteDataSource(row.id)">
                <template #reference>
                  <el-button :icon="Delete" type="danger"></el-button>
                </template>
              </el-popconfirm>
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
        @current-change="(val: number) => onPageChange(val, { params: { searchName: searchName } })"
        @size-change="(val: number) => onSizeChange(val, { params: { searchName: searchName } })"
      />
    </el-card>

    <!-- 对话框表单 -->
    <el-dialog v-model="toggleDialog.show" :title="toggleDialog.title" width="40%" @close="clean">
      <template #footer>
        <el-form
          ref="ruleFormRef"
          :model="dataSourceForm"
          :rules="rules"
          label-width="auto"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(ruleFormRef)"
        >
          <el-form-item label="名称" prop="name">
            <el-input v-model="dataSourceForm.name" placeholder="数据源配置名称"></el-input>
          </el-form-item>
          <el-form-item label="url" prop="url">
            <el-input v-model="dataSourceForm.url" placeholder="数据源配置 url"></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="dataSourceForm.username" placeholder="数据源配置用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="dataSourceForm.password" placeholder="数据源配置密码"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消</el-button>
            <el-button :loading="saveLoading" native-type="submit" type="primary">确认</el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped></style>
