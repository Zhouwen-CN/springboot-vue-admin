<script lang="ts" setup>
import { Search, Plus, Delete, Edit } from '@element-plus/icons-vue'
import { reqGetPage, reqRemoveById, reqRemoveByIds, type StudentTestVo } from '@/api/test/student'
import StudentTestForm from './components/StudentTestForm.vue'
import useDict from '@/hooks/useDictionary'

// 表单对话框
const formDialog = ref<InstanceType<typeof StudentTestForm>>()

// 分页查询参数
const pageParams = reactive({
  name: undefined,
  gender: undefined,
})

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
} = reqGetPage()

// 字典(可能没有，也可能有多个)
const { dictData: genderDictData, dictMap: genderDictMap, run:genderDictRun } = useDict(2)

// 分页搜索
function pageSearch() {
  refresh({ params: { ...pageParams } })
}

// 按照id删除
async function remove(id: number) {
  await reqRemoveById(id)
  refresh({ params: { ...pageParams } })
}

// 批量删除
const deleteIds = ref<number[]>([])
function handleSelectionChange(voList: StudentTestVo[]) {
  deleteIds.value = voList.map((voList) => voList.id)
}
async function removeBatch() {
  await reqRemoveByIds(deleteIds.value)
  refresh({ params: { ...pageParams } })
}

onMounted(() => {
  refresh({ params: { ...pageParams } })
  genderDictRun()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="pageSearch()">
        <el-form-item label="名称">
          <el-input v-model="pageParams.name" clearable placeholder="名称" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select
            v-model="pageParams.gender"
            clearable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option
              v-for="item in genderDictData"
              :key="item.data"
              :label="item.label"
              :value="item.data"
            >
              </el-option>
            </el-select>
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
        <el-button :icon="Plus" type="primary" @click="formDialog?.openDialog()">新建</el-button>
        <el-popconfirm title="是否删除？" @confirm="removeBatch">
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
        <el-table-column type="selection" width="45" />
        <el-table-column label="主键" prop="id"></el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="性别" prop="gender">
          <template #default="{ row }: { row: StudentTestVo }">
            {{ genderDictMap.get(row.gender) }}
          </template>
        </el-table-column>
        <el-table-column label="生日" prop="birthday"></el-table-column>
        <el-table-column label="简介" prop="intro"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }: { row: StudentTestVo }">
            <el-button-group>
              <el-button
                :icon="Edit"
                type="primary"
                @click="formDialog?.openDialog(row)"
              ></el-button>
              <el-popconfirm title="是否删除？" @confirm="remove(row.id)">
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
        @current-change="(val: number) => onPageChange(val, { params: { ...pageParams } })"
        @size-change="(val: number) => onSizeChange(val, { params: { ...pageParams } })"
      />
    </el-card>

    <StudentTestForm
      ref="formDialog"
      @refresh="refresh({ params: { ...pageParams } })"
      :genderDictData="genderDictData"
    ></StudentTestForm>
  </div>
</template>

<style lang="scss" scoped></style>
