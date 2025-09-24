<script lang="ts" setup>
import { Aim, Delete, Download, Edit, Refresh, Search, View } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { type DataSourceSelectorVo, reqGetDataSourceSelectorList } from '@/api/tool/datasource'
import {
  type CodegenTableImportForm,
  type CodegenTableSelectorVo,
  type CodegenTableVo,
  reqGetCodegenTablePage,
  reqGetCodegenTableSelectorList,
  reqImportCodegenTable,
  reqRemoveCodegenTableById,
  reqRemoveCodegenTableByIds,
  reqSyncCodegenColumnList,
  reqDownloadCodegen
} from '@/api/tool/codegen'
import useSettingStore from '@/stores/setting'
import CodegenEditTable from './components/CodegenEditTable.vue'
import CodegenPreview from './components/CodegenPreview.vue'
import MenuTreeSelect from '@/components/MenuTreeSelect.vue'
import type { AxiosResponse } from 'axios'

const { codegenConfig } = useSettingStore()

// 数据源选择器数据
const dataSourceSelectorList = ref<DataSourceSelectorVo[]>([])
// 代码生成表选择器数据
const codegenTableSelectorList = ref<CodegenTableSelectorVo[]>([])
// 对话框切换
const toggleDialog = ref(false)
// 导入代码生成表loading
const saveLoading = ref(false)
// 搜索关键字
const keyword = ref('')

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
} = reqGetCodegenTablePage()

// 根据关键字查询代码生成表
function searchByKeyword() {
  keyword.value = keyword.value.trim()
  refresh({ params: { keyword: keyword.value } })
}

// 代码生成导入-表单提交对象
const codegenTableImportForm = reactive<CodegenTableImportForm>({
  dataSourceId: undefined,
  parentMenuId: 0,
  author: codegenConfig.author,
  ignoreTablePrefix: codegenConfig.ignoreTablePrefix,
  ignoreColumnPrefix: codegenConfig.ignoreColumnPrefix,
  tableNames: []
})

// 导入表
async function importCodegenTable() {
  saveLoading.value = true
  try {
    if (codegenTableImportForm.dataSourceId) {
      const result = await reqGetCodegenTableSelectorList(codegenTableImportForm.dataSourceId)
      codegenTableSelectorList.value = result.data
      toggleDialog.value = true
    } else {
      ElMessage.warning('请选择数据源')
    }
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 导入代码生成表-表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof codegenTableImportForm>>({
  author: [
    { required: true, message: '作者不能为空', trigger: 'blur' },
    { min: 1, max: 20, message: '长度 1-20 之间', trigger: 'blur' }
  ],
  tableNames: [{ required: true, message: '选择器不能为空', trigger: 'submit' }]
})

// 导入代码生成表-表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqImportCodegenTable(codegenTableImportForm)
    toggleDialog.value = false
    refresh({ params: { keyword: keyword.value } })
    ElMessage.success('导入成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 删除代码生成表
async function deleteCodegenTableById(id: number) {
  await reqRemoveCodegenTableById(id)
  refresh({ params: { keyword: keyword.value } })
  ElMessage.success('操作成功')
}

// 批量删除代码生成表
const deleteIds = ref<number[]>([])
function handleSelectionChange(dataSourceList: CodegenTableVo[]) {
  deleteIds.value = dataSourceList.map((dataSource) => dataSource.id)
}
async function deleteCodegenTableByIds() {
  await reqRemoveCodegenTableByIds(deleteIds.value)
  refresh({ params: { keyword: keyword.value } })
  ElMessage.success('操作成功')
}

// 更新代码生成表
const codegenEditTableRef = ref<InstanceType<typeof CodegenEditTable>>()
function updateCodegenTable(row: CodegenTableVo) {
  codegenEditTableRef?.value?.showDrawer(row)
}

// 下载生成代码
async function downloadCodegen(id: number) {
  try {
    let link = document.createElement('a')
    const response = await reqDownloadCodegen(id)
    const fileName = getFileNameFormResponse('codegen.zip', response)
    const url = URL.createObjectURL(response.data)
    link.href = url
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    // 释放
    document.body.removeChild(link)
    URL.revokeObjectURL(url)
  } catch (error) {
    ElMessage.error('下载失败')
  }
  ElMessage.success('操作成功')
}

// 工具方法：从响应中获取文件名
function getFileNameFormResponse(defaultName: string, response: AxiosResponse) {
  let fileName = defaultName
  const contentDisposition = response.headers['content-disposition']
  if (contentDisposition && contentDisposition.includes('filename=')) {
    const matches = contentDisposition.match(/filename="?(.+)"?/) // 正则匹配文件名
    if (matches.length > 1) {
      fileName = matches[1]
    }
  }
  return fileName
}

// 代码生成预览
const codegenPreviewRef = ref<InstanceType<typeof CodegenPreview>>()
function previewCodegen(id: number) {
  codegenPreviewRef.value?.openDialog(id)
}

// 同步代码生成表字段
async function syncCodegenColumnList(id: number) {
  await reqSyncCodegenColumnList(id)
  refresh({ params: { keyword: keyword.value } })
  ElMessage.success('操作成功')
}

// 清空表单
function clean() {
  codegenTableImportForm.author = codegenConfig.author
  codegenTableImportForm.ignoreTablePrefix = codegenConfig.ignoreTablePrefix
  codegenTableImportForm.ignoreColumnPrefix = codegenConfig.ignoreColumnPrefix
  codegenTableImportForm.tableNames = []
  formRef.value?.clearValidate()
}

// 获取数据源选择器列表
onMounted(() => {
  reqGetDataSourceSelectorList().then((res) => {
    dataSourceSelectorList.value = res.data
    codegenTableImportForm.dataSourceId = dataSourceSelectorList.value[0]?.id
  })
  refresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="searchByKeyword()">
        <el-form-item label="关键字">
          <el-input v-model="keyword" clearable placeholder="关键字"></el-input>
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
        <el-select v-model="codegenTableImportForm.dataSourceId" style="width: 120px">
          <el-option
            v-for="(item, index) in dataSourceSelectorList"
            :key="index"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-button
          style="margin-left: 12px"
          :loading="saveLoading"
          :icon="Aim"
          type="primary"
          @click="importCodegenTable"
          >导入
        </el-button>
        <el-popconfirm title="是否删除？" @confirm="deleteCodegenTableByIds()">
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
        <el-table-column label="数据源名称" prop="dataSource"></el-table-column>
        <el-table-column label="表名称" prop="tableName"></el-table-column>
        <el-table-column label="表描述" prop="tableComment"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作" min-width="150px">
          <template #default="{ row }: { row: CodegenTableVo }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="updateCodegenTable(row)"></el-button>
              <el-button :icon="View" type="success" @click="previewCodegen(row.id)"> </el-button>
              <el-button :icon="Download" type="info" @click="downloadCodegen(row.id)"> </el-button>
              <el-popconfirm title="是否重新同步？" @confirm="syncCodegenColumnList(row.id)">
                <template #reference>
                  <el-button :icon="Refresh" type="warning"></el-button>
                </template>
              </el-popconfirm>
              <el-popconfirm title="是否删除？" @confirm="deleteCodegenTableById(row.id)">
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
        @current-change="(val: number) => onPageChange(val, { params: { keyword: keyword } })"
        @size-change="(val: number) => onSizeChange(val, { params: { keyword: keyword } })"
      />
    </el-card>

    <!-- 对话框表单-导入 -->
    <el-dialog v-model="toggleDialog" title="导入代码生成表" width="40%" @close="clean">
      <template #footer>
        <el-form
          ref="formRef"
          :model="codegenTableImportForm"
          :rules="rules"
          label-width="auto"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(formRef)"
        >
          <el-form-item label="上级菜单" prop="parentMenuId">
            <MenuTreeSelect v-model="codegenTableImportForm.parentMenuId" />
          </el-form-item>
          <el-form-item label="作者" prop="author">
            <el-input v-model="codegenTableImportForm.author" placeholder="作者"></el-input>
          </el-form-item>
          <el-form-item label="忽略表前缀" prop="ignoreTablePrefix">
            <el-input v-model="codegenTableImportForm.ignoreTablePrefix" placeholder="忽略表前缀">
            </el-input>
          </el-form-item>
          <el-form-item label="忽略字段前缀" prop="ignoreColumnPrefix">
            <el-input
              v-model="codegenTableImportForm.ignoreColumnPrefix"
              placeholder="忽略字段前缀"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="表名" prop="tableNames">
            <el-select
              v-model="codegenTableImportForm.tableNames"
              collapse-tags
              collapse-tags-tooltip
              filterable
              multiple
            >
              <el-option
                v-for="(item, index) in codegenTableSelectorList"
                :key="index"
                :value="item.name"
              >
                <span class="table-name">{{ item.name }}</span>
                <span class="table-comment">
                  {{ item.comment }}
                </span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog = false">取消</el-button>
            <el-button :loading="saveLoading" native-type="submit" type="primary">确认 </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>

    <!-- 代码生成编辑-抽屉 -->
    <CodegenEditTable ref="codegenEditTableRef"></CodegenEditTable>

    <!-- 代码生成预览-对话框 -->
    <CodegenPreview ref="codegenPreviewRef"></CodegenPreview>
  </div>
</template>

<style lang="scss" scoped>
.table-name {
  float: left;
}

.table-comment {
  float: right;
  color: var(--el-text-color-secondary);
}
</style>
