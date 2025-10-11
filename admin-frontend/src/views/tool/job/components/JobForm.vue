<script lang="ts" setup>
import { type JobForm, type JobVo, reqSave } from '@/api/tool/job'
import { type FormInstance, type FormRules } from 'element-plus'

// 保存后刷新事件
const emits = defineEmits(['refresh'])

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 表单对象
const form = reactive<JobForm>({
  id: undefined,
  name: undefined,
  cronExpression: undefined,
  jsScript: undefined,
  retryCount: 0,
  retryInterval: 0
})

// 表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
  name: [
    { required: true, message: '任务名称不能为空', trigger: 'blur' },
    { max: 32, message: '任务名称长度不能大于32', trigger: 'blur' }
  ],
  cronExpression: [
    { required: true, message: 'cron 表达式不能为空', trigger: 'blur' },
    { max: 32, message: 'cron 表达式长度不能大于32', trigger: 'blur' }
  ],
  jsScript: [
    { required: true, message: 'js脚本不能为空', trigger: 'blur' },
    { max: 65535, message: 'js脚本长度不能大于65535', trigger: 'blur' }
  ],
  retryCount: [{ required: true, message: '重试次数不能为空', trigger: 'blur' }],
  retryInterval: [{ required: true, message: '重试间隔不能为空', trigger: 'blur' }]
})

// 表单提交
const loading = ref(false)
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  loading.value = true
  try {
    await formEl.validate()
    await reqSave(form)
    emits('refresh')
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    loading.value = false
  }
}

// 清空表单
function clean() {
  toggleDialog.title = ''
  form.id = undefined
  form.name = undefined
  form.cronExpression = undefined
  form.jsScript = undefined
  form.retryCount = 0
  form.retryInterval = 0
  formRef.value?.clearValidate()
}

// 打开表单对话框
function openDialog(data?: JobVo) {
  if (data) {
    toggleDialog.title = '修改'
    // 数据回显
    form.id = data.id
    form.name = data.name
    form.cronExpression = data.cronExpression
    form.jsScript = data.jsScript
    form.retryCount = data.retryCount
    form.retryInterval = data.retryInterval
  } else {
    toggleDialog.title = '新增'
  }
  toggleDialog.show = true
}

defineExpose({
  openDialog
})
</script>

<template>
  <!-- 对话框表单 -->
  <el-dialog v-model="toggleDialog.show" :title="toggleDialog.title" width="40%" @close="clean">
    <template #footer>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="auto"
        style="padding: 0 20px"
        @submit.prevent="onSubmit(formRef)"
      >
        <el-form-item label="任务名称" prop="name">
          <el-input v-model="form.name" clearable placeholder="任务名称" />
        </el-form-item>
        <el-form-item label="cron 表达式" prop="cronExpression">
          <el-input v-model="form.cronExpression" clearable placeholder="cron 表达式" />
        </el-form-item>
        <el-form-item label="js脚本" prop="jsScript">
          <el-input
            v-model="form.jsScript"
            type="textarea"
            :autosize="{ minRows: 5, maxRows: 10 }"
            placeholder="js脚本"
          />
        </el-form-item>
        <el-form-item label="重试次数" prop="retryCount">
          <el-input v-model="form.retryCount" type="number" placeholder="重试次数" />
        </el-form-item>
        <el-form-item label="重试间隔" prop="retryInterval">
          <el-input v-model="form.retryInterval" type="number" placeholder="重试间隔" />
        </el-form-item>
        <el-form-item>
          <el-button @click="toggleDialog.show = false">取消</el-button>
          <el-button :loading="loading" native-type="submit" type="primary">确认</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
