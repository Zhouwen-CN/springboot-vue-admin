<script lang="ts" setup>
import { reqSave, type StudentTestForm, type StudentTestVo } from '@/api/test/student'
import { type FormRules, type FormInstance } from 'element-plus'
import type { DictDataSelectorVo } from '@/api/tool/dict'

// 保存后刷新事件
const emits = defineEmits(['refresh'])

// 字典数据(可能没有，也可能有多个)
defineProps({
  genderDictData: {
    type: Array as () => DictDataSelectorVo[],
    default: () => []
  }
})

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 表单对象
const form = reactive<StudentTestForm>({
  id: undefined,
  name: undefined,
  gender: undefined,
  birthday: undefined,
  intro: undefined
})

// 保存按钮加载中
const saveLoading = ref(false)

// 表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' },
    { max: 20, message: '名称长度不能大于20', trigger: 'blur' }
  ],
  gender: [{ required: true, message: '性别不能为空', trigger: 'blur' }],
  birthday: [{ required: true, message: '生日不能为空', trigger: 'blur' }],
  intro: [
    { required: true, message: '简介不能为空', trigger: 'blur' },
    { max: 65535, message: '简介长度不能大于65535', trigger: 'blur' }
  ]
})

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqSave(form)
    emits('refresh')
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 清空表单
function clean() {
  toggleDialog.title = ''
  form.id = undefined
  form.name = undefined
  form.gender = undefined
  form.birthday = undefined
  form.intro = undefined
  formRef.value?.clearValidate()
}

// 打开表单对话框
function openDialog(data?: StudentTestVo) {
  if (data) {
    toggleDialog.title = '修改'
    toggleDialog.show = true

    // 数据回显
    form.id = data.id
    form.name = data.name
    form.gender = data.gender
    form.birthday = data.birthday
    form.intro = data.intro
  } else {
    toggleDialog.title = '新增'
    toggleDialog.show = true
  }
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" clearable placeholder="名称" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" clearable placeholder="请选择" style="width: 120px">
            <el-option
              v-for="item in genderDictData"
              :key="item.data"
              :label="item.label"
              :value="item.data"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-date-picker
            v-model="form.birthday"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            clearable
            placeholder="生日"
          />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input
            v-model="form.intro"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="简介"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="toggleDialog.show = false">取消</el-button>
          <el-button :loading="saveLoading" native-type="submit" type="primary">确认</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
