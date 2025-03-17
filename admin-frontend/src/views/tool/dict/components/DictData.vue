<script lang="ts" setup>
import {
  type DictData,
  type DictDataForm,
  reqGetDictDataPageByTypeId,
  reqRemoveDictDataById,
  reqRemoveDictDataByIds,
  reqSaveDictData
} from '@/api/tool/dict'
import {Delete, Edit, Search} from '@element-plus/icons-vue'
import {type FormInstance, type FormRules} from 'element-plus'

// 搜索关键字
const searchLabel = ref('')
// 抽屉可见
const drawerVisible = ref(false)
// 类型id
const typeId = ref<number>(-1)
// 字典数据表单
const dictDataForm = reactive<DictDataForm>({
  id: undefined,
  typeId: -1,
  label: '',
  value: 0,
  sort: 0
})
// 字典数据弹窗
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 保存字典数据按钮loading
const saveLoading = ref(false)

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
} = reqGetDictDataPageByTypeId()

// 查询字典数据
function searchByLabel() {
  refresh({params: {typeId: typeId.value, label: searchLabel.value}})
}

// 删除字典数据
async function removeDictData(id: number) {
  try {
    await reqRemoveDictDataById(id)
    refresh({params: {typeId: typeId.value, label: searchLabel.value}})
    ElMessage.success('操作成功')
  } catch (e) {
    // do nothing
  }
}

// 批量删除字典数据
const removeBatchDictDataIds = ref<number[]>([])
function handleSelectionChange(dictDatas: DictData[]) {
  removeBatchDictDataIds.value = dictDatas.map((dictData) => dictData.id)
}
async function removeBatchDictData() {
  try {
    await reqRemoveDictDataByIds(removeBatchDictDataIds.value)
    refresh({params: {typeId: typeId.value, label: searchLabel.value}})
    ElMessage.success('操作成功')
  } catch (e) {
    // do noting
  }
}

// 添加字典数据按钮
function addDictData() {
  toggleDialog.show = true
  toggleDialog.title = '新增字典数据'
}

// 更新字典数据按钮
function modifyDictData(row: DictData) {
  toggleDialog.show = true
  toggleDialog.title = '更新字典数据'
  dictDataForm.id = row.id
  dictDataForm.typeId = row.typeId
  dictDataForm.label = row.label
  dictDataForm.value = row.value
  dictDataForm.sort = row.sort
}

// 表单校验
const dictDataFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof dictDataForm>>({
  label: [{required: true, message: '请输入标签键', trigger: 'blur'}],
  value: [{required: true, message: '请输入标签值', trigger: 'blur'}],
  sort: [{required: true, message: '请输入排序值', trigger: 'blur'}]
})

// 表单提交
async function dictDataFormSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqSaveDictData(dictDataForm)
    refresh({params: {typeId: typeId.value, label: searchLabel.value}})
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 清除弹窗数据
function dictDataDialogClean() {
  dictDataForm.id = undefined
  dictDataForm.typeId = typeId.value
  dictDataForm.label = ''
  dictDataForm.value = 0
  dictDataForm.sort = 0
  dictDataFormRef.value?.clearValidate()
}

// 打开抽屉
function showDrawer(id: number) {
  typeId.value = id
  dictDataForm.typeId = id
  drawerVisible.value = true
  searchLabel.value = ''
  refresh({params: {typeId: typeId.value}})
}

defineExpose({
  showDrawer
})
</script>

<template>
  <el-drawer v-model="drawerVisible" size="50%" title="字典数据">
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline @submit.prevent="searchByLabel()">
        <el-form-item label="标签键：">
          <el-input v-model="searchLabel" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" :loading="pageLoading"
                     native-type="submit"
                     type="primary">搜索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格上面的按钮 -->
      <div>
        <el-button type="primary"
                   @click="addDictData">添加字典数据
        </el-button>
        <el-popconfirm title="是否删除？" @confirm="removeBatchDictData">
          <template #reference>
            <el-button type="danger">批量删除</el-button>
          </template>
        </el-popconfirm>
      </div>

      <!-- 表格 -->
      <el-table :border="true" :data="pageData" show-overflow-tooltip
                style="margin-top: 16px"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="ID" min-width="40px"
                         prop="id"></el-table-column>
        <el-table-column label="标签键" min-width="40px" prop="label">
        </el-table-column>
        <el-table-column label="标签值" min-width="40px"
                         prop="value"></el-table-column>
        <el-table-column label="排序" min-width="40px"
                         prop="sort"></el-table-column>
        <el-table-column label="更新时间"
                         prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }: { row: DictData }">
            <el-button-group>
              <el-button :icon="Edit" size="small" type="primary"
                         @click="modifyDictData(row)">
              </el-button>
              <el-popconfirm title="是否删除？"
                             @confirm="removeDictData(row.id)">
                <template #reference>
                  <el-button :icon="Delete" size="small"
                             type="danger"></el-button>
                </template>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination v-model:current-page="current"
                     v-model:page-size="size"
                     :page-sizes="sizeOption" :total="total" background
                     layout="prev, pager, next, ->, total, sizes"
                     style="margin-top: 16px"
                     @current-change="(val) => onPageChange(val, { params: { typeId: typeId, label: searchLabel } })"
                     @size-change="(val) => onSizeChange(val, { params: { typeId: typeId, label: searchLabel } })"/>
    </el-card>

    <!-- 对话框表单 -->
    <el-dialog v-model="toggleDialog.show" :title="toggleDialog.title"
               width="40%" @close="dictDataDialogClean">
      <template #footer>
        <el-form
            ref="dictDataFormRef"
            :model="dictDataForm"
            :rules="rules"
            label-width="80px"
            style="padding: 0 20px"
            @submit.prevent="dictDataFormSubmit(dictDataFormRef)">
          <el-form-item label="标签键" prop="label">
            <el-input v-model="dictDataForm.label"
                      placeholder="请输入标签键"></el-input>
          </el-form-item>
          <el-form-item label="标签值" prop="value">
            <el-input v-model="dictDataForm.value" type="number"
                      placeholder="请输入标签键"></el-input>
          </el-form-item>
          <el-form-item label="排序" prop="sort">
            <el-input v-model="dictDataForm.sort"
                      type="number"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
                @click="toggleDialog.show = false">取消
            </el-button>
            <el-button :loading="saveLoading"
                       native-type="submit"
                       type="primary">确认
            </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </el-drawer>
</template>

<style lang="scss" scoped></style>
