<script lang="ts" setup>
import {
  CircleCheck,
  Delete,
  Edit,
  Plus,
  Search,
  VideoPause,
  VideoPlay
} from '@element-plus/icons-vue'
import {
  type JobVo,
  reqGetPage,
  reqModifyEnable,
  reqRemoveById,
  reqTriggerJobOnce
} from '@/api/tool/job'
import JobForm from './components/JobForm.vue'
const router = useRouter()

// 表单对话框
const formDialog = ref<InstanceType<typeof JobForm>>()

// 分页查询参数
const pageParams = reactive({
  name: undefined,
  jobEnable: undefined
})

// 分页
const { loading, current, total, size, sizeOption, data, refresh, onPageChange, onSizeChange } =
  reqGetPage()

// 分页搜索
function pageSearch() {
  refresh({ params: { ...pageParams } })
}

// 按照id删除
async function remove(row: JobVo) {
  row.loading = true
  try {
    await reqRemoveById(row.id, row.name)
    refresh({ params: { ...pageParams } })
  } catch (error) {
    // do nothing
  } finally {
    row.loading = false
  }
}

// 修改任务状态
async function modifyJobEnable(row: JobVo) {
  row.loading = true
  try {
    await reqModifyEnable(row.id, {
      name: row.name,
      jobEnable: !row.jobEnable
    })
    refresh({ params: { ...pageParams } })
  } catch (error) {
    // do nothing
  } finally {
    row.loading = false
  }
}

// 触发一次任务
async function triggerJobOnce(row: JobVo) {
  row.loading = true
  try {
    await reqTriggerJobOnce(row.id)
  } catch (error) {
    // do nothing
  } finally {
    row.loading = false
  }
}

// 跳转到调度日志
function toJobLogView(row: JobVo) {
  router.push({
    path: '/log/job',
    query: {
      jobId: row.id
    }
  })
}

onMounted(() => {
  refresh({ params: { ...pageParams } })
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="pageSearch()">
        <el-form-item label="任务名称">
          <el-input v-model="pageParams.name" clearable placeholder="任务名称" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select
            v-model="pageParams.jobEnable"
            clearable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option :value="1" label="开启" />
            <el-option :value="0" label="关闭" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" :loading="loading" native-type="submit" type="primary"
            >搜索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格上面的按钮 -->
      <div>
        <el-button :icon="Plus" type="primary" @click="formDialog?.openDialog()">新建</el-button>
      </div>

      <!-- 表格 -->
      <div style="margin-top: 16px">
        <el-table :border="true" :data="data" show-overflow-tooltip>
          <el-table-column label="主键" prop="id"></el-table-column>
          <el-table-column label="任务名称" prop="name">
            <template #default="{ row }: { row: JobVo }">
              <span class="job-name" @click="toJobLogView(row)">{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="处理器名称" prop="handlerName"></el-table-column>
          <el-table-column label="cron 表达式" prop="cronExpression"></el-table-column>
          <el-table-column label="重试次数" prop="retryCount"></el-table-column>
          <el-table-column label="重试间隔" prop="retryInterval"></el-table-column>
          <el-table-column label="创建时间" prop="createTime"></el-table-column>
          <el-table-column label="更新时间" prop="updateTime"></el-table-column>
          <el-table-column label="操作" min-width="150px">
            <template #default="{ row }: { row: JobVo }">
              <el-button-group>
                <el-button
                  :icon="Edit"
                  type="primary"
                  @click="formDialog?.openDialog(row)"
                ></el-button>
                <el-button
                  :icon="row.jobEnable ? VideoPause : VideoPlay"
                  :loading="row.loading"
                  :type="row.jobEnable ? 'success' : 'info'"
                  @click="modifyJobEnable(row)"
                ></el-button>
                <el-button
                  :icon="CircleCheck"
                  :loading="row.loading"
                  type="warning"
                  @click="triggerJobOnce(row)"
                ></el-button>
                <el-popconfirm title="是否删除？" @confirm="remove(row)">
                  <template #reference>
                    <el-button :icon="Delete" :loading="row.loading" type="danger"></el-button>
                  </template>
                </el-popconfirm>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>

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

    <JobForm ref="formDialog" @refresh="refresh({ params: { ...pageParams } })"></JobForm>
  </div>
</template>

<style lang="scss" scoped>
.job-name {
  color: var(--el-color-primary);
  cursor: pointer;
}
</style>
