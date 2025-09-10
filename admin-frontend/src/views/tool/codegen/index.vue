<script lang="ts" setup>
import { Download } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { reqGetDataSourceSelectorList, type DataSourceSelectorVo } from '@/api/tool/datasource'
import {
  reqGetCodegenTableSelectorList,
  reqImportCodegenTable,
  type CodegenTableSelectorVo,
  type CodegenTableImportForm
} from '@/api/tool/codegen'
import useSettingStore from '@/stores/setting'
const { codegenConfig } = useSettingStore()

// 数据源选择器数据
const dataSourceSelectorList = ref<DataSourceSelectorVo[]>([])
// 代码生成表选择器数据
const codegenTableSelectorList = ref<CodegenTableSelectorVo[]>([])
// 对话框切换
const toggleDialog = ref(false)
const saveLoading = ref(false)

// 代码生成导入-表单提交对象
const codegenTableImportForm = reactive<CodegenTableImportForm>({
  dataSourceId: undefined,
  author: codegenConfig.author,
  ignoreTablePrefix: codegenConfig.ignoreTablePrefix,
  ignoreColumnPrefix: codegenConfig.ignoreColumnPrefix,
  basePackage: codegenConfig.basePackage,
  tableNames: []
})

// 导入表
async function importCodegenTable() {
  if (codegenTableImportForm.dataSourceId) {
    const result = await reqGetCodegenTableSelectorList(codegenTableImportForm.dataSourceId)
    codegenTableSelectorList.value = result.data
    toggleDialog.value = true
  } else {
    ElMessage.warning('请选择数据源')
  }
}

// 代码生成导入-表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof codegenTableImportForm>>({
  author: [
    { required: true, message: '作者不能为空', trigger: 'blur' },
    { min: 1, max: 20, message: '长度 1-20 之间', trigger: 'blur' }
  ],
  basePackage: [{ required: true, message: '基础包名不能为空', trigger: 'blur' }],
  tableNames: [{ required: true, message: '选择器不能为空', trigger: 'submit' }]
})

// 代码生成导入-表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqImportCodegenTable(codegenTableImportForm)
    ElMessage.success('导入成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 清空表单
function clean() {
  codegenTableImportForm.author = codegenConfig.author
  codegenTableImportForm.ignoreTablePrefix = codegenConfig.ignoreTablePrefix
  codegenTableImportForm.ignoreColumnPrefix = codegenConfig.ignoreTablePrefix
  codegenTableImportForm.basePackage = codegenConfig.basePackage
  codegenTableImportForm.tableNames = []
  formRef.value?.clearValidate()
}

// 获取数据源选择器列表
onMounted(() => {
  reqGetDataSourceSelectorList().then((res) => {
    dataSourceSelectorList.value = res.data
    codegenTableImportForm.dataSourceId = dataSourceSelectorList.value[0]?.id
  })
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="importCodegenTable">
        <el-form-item label="数据源" prop="dataSourceId">
          <el-select v-model="codegenTableImportForm.dataSourceId" style="width: 120px">
            <el-option
              v-for="(item, index) in dataSourceSelectorList"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Download" native-type="submit" type="primary">导入 </el-button>
        </el-form-item>
      </el-form>
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
          <el-form-item label="作者" prop="author">
            <el-input v-model="codegenTableImportForm.author" placeholder="作者"> </el-input>
          </el-form-item>
          <el-form-item label="基础包名" prop="basePackage">
            <el-input v-model="codegenTableImportForm.basePackage" placeholder="基础包名">
            </el-input>
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
              multiple
              collapse-tags
              collapse-tags-tooltip
              filterable
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
            <el-button :loading="saveLoading" native-type="submit" type="primary">确认</el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
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
