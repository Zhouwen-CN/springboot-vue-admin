<script lang="ts" setup>
import { Delete, Edit, Plus, Search } from '@element-plus/icons-vue'
import {
  type DictTypeForm,
  type DictTypeVo,
  reqGetDictTypePage,
  reqRemoveDictTypeById,
  reqRemoveDictTypeByIds,
  reqSaveDictType
} from '@/api/tool/dict'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import DictData from './components/DictData.vue'

// 搜索关键字
const searchName = ref('')
// 弹窗切换
const toggleDialog = reactive({
  show: false,
  title: ''
})
// 字典类型表单
const dictTypeForm = reactive<DictTypeForm>({
  id: undefined,
  name: '',
  dictEnable: true
})

// 保存字典按钮loading
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
} = reqGetDictTypePage()

// 查询字典类型
function searchByKeyword() {
  searchName.value = searchName.value.trim()
  refresh({ params: { name: searchName.value } })
}

// 删除字典类型
async function removeDictType(id: number) {
  await reqRemoveDictTypeById(id)
  refresh({ params: { name: searchName.value } })
  ElMessage.success('操作成功')
}

// 批量删除字典类型
const deleteIds = ref<number[]>([])
function handleSelectionChange(dictTypes: DictTypeVo[]) {
  deleteIds.value = dictTypes.map((dictType) => dictType.id)
}
async function removeBatchDictType() {
  await reqRemoveDictTypeByIds(deleteIds.value)
  refresh({ params: { name: searchName.value } })
  ElMessage.success('操作成功')
}

// 新增字典类型按钮
function addDictType() {
  toggleDialog.show = true
  toggleDialog.title = '新增字典类型'
}

// 更新字典类型按钮
function modifyDictType(row: DictTypeVo) {
  toggleDialog.show = true
  toggleDialog.title = '更新字典类型'
  dictTypeForm.id = row.id
  dictTypeForm.name = row.name
  dictTypeForm.dictEnable = row.dictEnable
}

// 表单校验
const dictTypeFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof dictTypeForm>>({
  name: [{ required: true, message: '请输入字典类型', trigger: 'blur' }]
})

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqSaveDictType(dictTypeForm)
    refresh({ params: { name: searchName.value } })
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 表单清空
function dialogClean() {
  toggleDialog.title = ''
  dictTypeForm.id = undefined
  dictTypeForm.name = ''
  dictTypeForm.dictEnable = true
  dictTypeFormRef.value?.clearValidate()
}

// 字典数据抽屉
const dictData = ref<InstanceType<typeof DictData>>()

// 打开抽屉
function openDrawer(typeId: number) {
  dictData.value?.showDrawer(typeId)
}

onMounted(() => {
  refresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="searchByKeyword()">
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
        <el-button :icon="Plus" type="primary" @click="addDictType">新建 </el-button>
        <el-popconfirm title="是否删除？" @confirm="removeBatchDictType">
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
        <el-table-column label="字典名称" prop="name">
          <template #default="{ row }: { row: DictTypeVo }">
            <span class="dict-type" @click="openDrawer(row.id)">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="dictEnable">
          <template #default="{ row }: { row: DictTypeVo }">
            <el-tag size="large" :type="row.dictEnable ? 'success' : 'danger'"
              >{{ row.dictEnable ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }: { row: DictTypeVo }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="modifyDictType(row)"></el-button>
              <el-popconfirm title="是否删除？" @confirm="removeDictType(row.id)">
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
        @current-change="(val: number) => onPageChange(val, { params: { name: searchName } })"
        @size-change="(val: number) => onSizeChange(val, { params: { name: searchName } })"
      />
    </el-card>

    <!-- 对话框表单 -->
    <el-dialog
      v-model="toggleDialog.show"
      :title="toggleDialog.title"
      width="40%"
      @close="dialogClean"
    >
      <template #footer>
        <el-form
          ref="dictTypeFormRef"
          :model="dictTypeForm"
          :rules="rules"
          label-width="auto"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(dictTypeFormRef)"
        >
          <el-form-item label="字典名称" prop="name">
            <el-input v-model="dictTypeForm.name" placeholder="字典名称"></el-input>
          </el-form-item>
          <el-form-item label="是否开启" prop="dictEnable">
            <el-switch v-model="dictTypeForm.dictEnable"></el-switch>
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消 </el-button>
            <el-button :loading="saveLoading" native-type="submit" type="primary">确认 </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>

    <!-- 抽屉 -->
    <dict-data ref="dictData"></dict-data>
  </div>
</template>

<style lang="scss" scoped>
.dict-type {
  color: var(--el-color-primary);
  cursor: pointer;
}
</style>
