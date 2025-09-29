<#--@formatter:off-->
<script lang="ts" setup>
import { reqSave, type ${table.className}Form, type ${table.className}Vo } from '@/api${package.jsBasePackage}'
import { type FormRules, type FormInstance } from 'element-plus'
<#-- 字典列表 -->
<#assign dictList = columns?filter(column -> column.dictTypeId??)>
<#if (dictList!?size > 0)>
import type { DictDataSelectorVo } from '@/api/tool/dict'
</#if>

// 保存后刷新事件
const emits = defineEmits(['refresh'])

<#if (dictList!?size > 0)>
// 字典数据
defineProps({
  <#list dictList as dict>
  ${dict.javaField}DictData: {
    type: Array as () => DictDataSelectorVo[],
    required: true
  },
</#list>
})

</#if>
// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 表单对象
const form = reactive<${table.className}Form>({
<#list columns?filter(column -> column.updateField) as column>
<#if column.jsType == 'boolean'>
  ${column.javaField}: false,
<#else>
  ${column.javaField}: undefined,
</#if>
</#list>
})

// 表单校验
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
<#list columns?filter(column -> column.updateField && !column.primaryKey && column.jsType != 'boolean') as column>
  ${column.javaField}: [
    { required: true, message: '${column.columnComment}不能为空', trigger: 'blur' },
  <#if column.columnLength??>
    { max: ${column.columnLength}, message: '${column.columnComment}长度不能大于${column.columnLength}', trigger: 'blur' }
  </#if>
  ],
</#list>
})

// 表单提交
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
<#list columns?filter(column -> column.updateField) as column>
  form.${column.javaField} = undefined
</#list>
  formRef.value?.clearValidate()
}

// 打开表单对话框
function openDialog(data?: ${table.className}Vo) {
  if (data) {
    toggleDialog.title = '修改'
    // 数据回显
  <#list columns?filter(column -> column.updateField) as column>
    form.${column.javaField} = data.${column.javaField}
  </#list>
  } else {
    toggleDialog.title = '新增'
  }
  toggleDialog.show = true
}

// save loading
const loading = defineModel('loading', {
  type: Boolean,
  required: true
})

defineExpose({
  openDialog
})
</script>

<#function getHtmlByType column variableName>
  <#-- 字典 -->
  <#if column.dictTypeId??>
    <#return '<el-select v-model="${variableName}.${column.javaField}" clearable placeholder="请选择" style="width: 120px">
            <el-option
              v-for="item in ${column.javaField}DictData"
              :key="item.data"
              :label="item.label"
              :value="item.data"
            >
              </el-option>
            </el-select>'>
  <#-- 时间选择器 -->
  <#elseif column.javaType='LocalDateTime' && column.htmlType='datetime'>
    <#return '<el-date-picker
            v-model="${variableName}.${column.javaField}"
            type="datetime"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            clearable
            placeholder="${column.columnComment}"
          />'>
  <#-- 文本域 -->
  <#elseif column.javaType='String' && column.htmlType='textarea'>
    <#return '<el-input
            v-model="${variableName}.${column.javaField}"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="${column.columnComment}"
          />'>
  <#-- 滑块切换 -->
  <#elseif column.javaType='Boolean' && column.htmlType='switch'>
    <#return '<el-switch v-model="${variableName}.${column.javaField}" />'>
  <#-- 默认input -->
  <#else>
    <#return '<el-input v-model="${variableName}.${column.javaField}" clearable placeholder="${column.columnComment}" />'>
  </#if>
</#function>
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
      <#list columns?filter(column -> column.updateField && !column.primaryKey) as column>
        <el-form-item label="${column.columnComment}" prop="${column.javaField}">
          ${getHtmlByType(column, 'form')}
        </el-form-item>
      </#list>
        <el-form-item>
          <el-button @click="toggleDialog.show = false">取消</el-button>
          <el-button :loading="loading" native-type="submit" type="primary">确认</el-button>
        </el-form-item>
      </el-form>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped></style>
