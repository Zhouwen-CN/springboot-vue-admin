<script lang="ts" setup>
import { Search } from '@element-plus/icons-vue'
import { type JobLogParam, reqGetJobLogPage } from '@/api/log'
import { reqGetJobSelector, type JobSelectorVo } from '@/api/tool/job'
const route = useRoute()

// 分页查询参数
const pageParams = reactive<JobLogParam>({
  jobId: undefined,
  status: undefined,
  startTime: undefined,
  endTime: undefined
})

// 任务选择器
const jobSelectorVo = ref<JobSelectorVo[]>()

// 分页
const { loading, current, total, size, sizeOption, data, refresh, onPageChange, onSizeChange } =
  reqGetJobLogPage()

// 分页搜索
function pageSearch() {
  refresh({ params: { ...pageParams } })
}

onMounted(() => {
  reqGetJobSelector().then((res) => {
    jobSelectorVo.value = res.data
  })
  const jobId = route.query.jobId
  if (jobId) {
    pageParams.jobId = Number(jobId)
  }
  refresh({ params: { ...pageParams } })
})
</script>
<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="pageSearch()">
        <el-form-item label="任务名称">
          <el-select
            v-model="pageParams.jobId"
            clearable
            filterable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option
              v-for="item in jobSelectorVo"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select
            v-model="pageParams.status"
            clearable
            placeholder="请选择"
            style="width: 120px"
          >
            <el-option label="成功" value="1" />
            <el-option label="失败" value="0" />
            <el-option label="运行中" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="pageParams.startTime"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            clearable
            placeholder="创建时间"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="pageParams.endTime"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            clearable
            placeholder="创建时间"
          />
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" :loading="loading" native-type="submit" type="primary"
            >搜索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格 -->
      <el-table :border="true" :data="data" show-overflow-tooltip>
        <el-table-column label="任务名称" prop="jobName"></el-table-column>
        <el-table-column label="处理器名称" prop="handlerName"></el-table-column>
        <el-table-column label="处理器参数" prop="handlerParam"></el-table-column>
        <el-table-column label="第几次执行" prop="fireNum"></el-table-column>
        <el-table-column label="执行耗时" prop="time"></el-table-column>
        <el-table-column label="任务状态" prop="status"></el-table-column>
        <el-table-column label="结果数据" prop="result"></el-table-column>
        <el-table-column label="开始时间" prop="startTime"></el-table-column>
        <el-table-column label="结束时间" prop="endTime"></el-table-column>
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
  </div>
</template>

<style lang="scss" scoped></style>
