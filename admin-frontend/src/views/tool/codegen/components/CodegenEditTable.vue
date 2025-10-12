<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus'
import {
  type CodegenColumnVo,
  type CodegenTableForm,
  type CodegenTableVo,
  reqGetCodegenColumnList,
  reqUpdateCodegenTable
} from '@/api/tool/codegen'
import { type DictTypeSelectorVo, reqGetDictTypeSelectorList } from '@/api/tool/dict'
import useSettingStore from '@/stores/setting'
import MenuTreeSelect from '@/components/MenuTreeSelect.vue'

const {
  codegenConfig: { jsTypeList, javaTypeList, htmlTypeList, selectConditionList }
} = useSettingStore()

// 抽屉开关
const drawerVisible = ref(false)
// 激活的标签
const activeName = ref('colums')
const saveLoading = ref(false)
// 表单数据
const formData = reactive<CodegenTableForm>({
  table: {} as CodegenTableVo,
  columns: []
})
const dictTypeSelectorList = ref<DictTypeSelectorVo[]>([])

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqUpdateCodegenTable(formData)
    drawerVisible.value = false
    ElMessage.success('保存成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof formData>>({
  'table.tableName': [{ required: true, message: '请输入表名称', trigger: 'blur' }],
  'table.tableComment': [{ required: true, message: '请输入类注释', trigger: 'blur' }],
  'table.className': [{ required: true, message: '请输入类名称', trigger: 'blur' }],
  'table.author': [{ required: true, message: '请输入作者', trigger: 'blur' }],
  'table.businessName': [{ required: true, message: '请输入业务名称', trigger: 'blur' }]
})

// 打开抽屉
async function showDrawer(codegenTableVo: CodegenTableVo) {
  drawerVisible.value = true
  formData.table = codegenTableVo
  const result = await reqGetCodegenColumnList(codegenTableVo.id)
  formData.columns = result.data
}

// 关闭抽屉
function closeDrawer() {
  formRef.value?.clearValidate()
  formData.table = {} as CodegenTableVo
  formData.columns = []
}

// 当字典类型有值时，htmlType置为select
function dictTypeChange(row: CodegenColumnVo) {
  if (row.dictTypeId) {
    row.htmlType = 'select'
  }
}

onMounted(() => {
  reqGetDictTypeSelectorList().then((result) => {
    dictTypeSelectorList.value = result.data
  })
})

defineExpose({
  showDrawer
})
</script>

<template>
  <el-drawer v-model="drawerVisible" size="75%" title="代码生成配置" @closed="closeDrawer">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="auto"
      style="padding: 0 20px"
      @submit.prevent="onSubmit(formRef)"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="基础信息" name="basicInfo">
          <el-form-item label="数据源名称" prop="table.dataSource">
            <el-input v-model="formData.table.dataSource" disabled> </el-input>
          </el-form-item>
          <el-form-item label="上级菜单" prop="table.parentMenuId">
            <MenuTreeSelect v-model="formData.table.parentMenuId" />
          </el-form-item>
          <el-form-item label="表名称" prop="table.tableName">
            <el-input v-model="formData.table.tableName" placeholder="请输入表名称"> </el-input>
          </el-form-item>
          <el-form-item label="表描述" prop="table.tableComment">
            <el-input v-model="formData.table.tableComment" placeholder="请输入表描述"> </el-input>
          </el-form-item>
          <el-form-item label="类名称" prop="table.className">
            <el-input v-model="formData.table.className" placeholder="请输入类名称"> </el-input>
          </el-form-item>
          <el-form-item label="作者" prop="table.author">
            <el-input v-model="formData.table.author" placeholder="请输入作者"> </el-input>
          </el-form-item>
          <el-form-item
            label="业务名称"
            prop="table.businessName"
            v-tip="`作为url前缀 和 前端文件夹名称`"
          >
            <el-input v-model="formData.table.businessName" placeholder="请输入业务名称">
            </el-input>
          </el-form-item>
          <el-form-item label="忽略表前缀" prop="table.ignoreTablePrefix">
            <el-input v-model="formData.table.ignoreTablePrefix" placeholder="请输入忽略表前缀">
            </el-input>
          </el-form-item>
          <el-form-item label="忽略字段前缀" prop="table.ignoreColumnPrefix">
            <el-input v-model="formData.table.ignoreColumnPrefix" placeholder="请输入忽略字段前缀">
            </el-input>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="字段信息" name="colums">
          <el-table :data="formData.columns" show-overflow-tooltip>
            <el-table-column label="字段名称" prop="columnName"> </el-table-column>
            <el-table-column label="字段描述" prop="columnComment">
              <template #default="{ row, $index }: { row: CodegenColumnVo; $index: number }">
                <el-form-item
                  style="margin-bottom: 0"
                  :prop="`columns[${$index}].columnComment`"
                  :rules="{ required: true, message: '请输入字段描述', trigger: 'blur' }"
                >
                  <el-input v-model="row.columnComment"> </el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="物理类型" prop="dbType"> </el-table-column>
            <el-table-column label="属性名" prop="javaField">
              <template #default="{ row, $index }: { row: CodegenColumnVo; $index: number }">
                <el-form-item
                  style="margin-bottom: 0"
                  :prop="`columns[${$index}].javaField`"
                  :rules="{ required: true, message: '请输入属性名', trigger: 'blur' }"
                >
                  <el-input v-model="row.javaField"> </el-input>
                </el-form-item>
              </template>
            </el-table-column>
            <el-table-column label="Java类型" prop="javaType">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-select v-model="row.javaType">
                  <el-option
                    v-for="(item, index) in javaTypeList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="Js类型" prop="jsType">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-select v-model="row.jsType">
                  <el-option
                    v-for="(item, index) in jsTypeList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="新增" prop="insertField" min-width="45px">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-checkbox v-model="row.insertField" false-value="false" true-value="true" />
              </template>
            </el-table-column>
            <el-table-column label="编辑" prop="updateField" min-width="45px">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-checkbox v-model="row.updateField" false-value="false" true-value="true" />
              </template>
            </el-table-column>
            <el-table-column label="Html类型" prop="htmlType">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-select v-model="row.htmlType">
                  <el-option
                    v-for="(item, index) in htmlTypeList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="列表" prop="selectResultField" min-width="45px">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-checkbox
                  v-model="row.selectResultField"
                  false-value="false"
                  true-value="true"
                />
              </template>
            </el-table-column>
            <el-table-column label="查询" prop="selectConditionField" min-width="45px">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-checkbox
                  v-model="row.selectConditionField"
                  false-value="false"
                  true-value="true"
                />
              </template>
            </el-table-column>
            <el-table-column label="查询条件" prop="selectCondition">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-select v-model="row.selectCondition">
                  <el-option
                    v-for="(item, index) in selectConditionList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="字典类型" prop="dictTypeId">
              <template #default="{ row }: { row: CodegenColumnVo }">
                <el-select v-model="row.dictTypeId" clearable @change="dictTypeChange(row)">
                  <el-option
                    v-for="(item, index) in dictTypeSelectorList"
                    :key="index"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <el-form-item style="margin-top: 16px">
        <el-button @click="drawerVisible = false">取消</el-button>
        <el-button :loading="saveLoading" native-type="submit" type="primary">提交</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<style lang="scss" scoped></style>
