<#--@formatter:off-->
<script lang="ts" setup>
import { Search, Plus, Delete, Edit } from '@element-plus/icons-vue'
import { reqGetPage, reqRemoveById, reqRemoveByIds, type ${table.className}Vo } from '@/api${package.jsBasePackage}'
import ${table.className}Form from './components/${table.className}Form.vue'
<#-- 字典列表 -->
<#assign dictList = columns?filter(column -> column.dictTypeId??)>
<#if (dictList!?size > 0)>
import useDict from '@/hooks/useDictionary'
</#if>

// 表单对话框
const formDialog = ref<InstanceType<typeof ${table.className}Form>>()

// 分页查询参数
const pageParams = reactive({
<#list columns?filter(column -> column.selectConditionField) as column>
  ${column.javaField}: undefined,
</#list>
})

// 分页
const {
  loading,
  current,
  total,
  size,
  sizeOption,
  data,
  refresh,
  onPageChange,
  onSizeChange
} = reqGetPage()

<#if (dictList!?size > 0)>
// 字典
<#list dictList as dict>
const { dictData: ${dict.javaField}DictData, dictMap: ${dict.javaField}DictMap, run:${dict.javaField}DictRun } = useDict(${dict.dictTypeId})

</#list>
</#if>
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
<#-- 获取主键 -->
<#assign primaryKey= columns?filter(column -> column.primaryKey)?first>
const deleteIds = ref<number[]>([])
function handleSelectionChange(voList: ${table.className}Vo[]) {
  deleteIds.value = voList.map((voList) => voList.${primaryKey.javaField})
}
async function removeBatch() {
  await reqRemoveByIds(deleteIds.value)
  refresh({ params: { ...pageParams } })
}

onMounted(() => {
  refresh({ params: { ...pageParams } })
<#if (dictList!?size > 0)>
<#list dictList as dict>
  ${dict.javaField}DictRun()
</#list>
</#if>
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
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline label-width="auto" @submit.prevent="pageSearch()">
    <#list columns?filter(column -> column.selectConditionField) as column>
        <el-form-item label="${column.columnComment}">
          ${getHtmlByType(column, 'pageParams')}
        </el-form-item>
    </#list>
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
        :data="data"
        show-overflow-tooltip
        style="margin-top: 16px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="45px" />
    <#list columns?filter(column -> column.selectResultField) as column>
      <#if column.dictTypeId??>
        <el-table-column label="${column.columnComment}" prop="${column.javaField}">
          <template #default="{ row }: { row: ${table.className}Vo }">
            {{ ${column.javaField}DictMap.get(row.${column.javaField}) }}
          </template>
        </el-table-column>
      <#else>
        <el-table-column label="${column.columnComment}" prop="${column.javaField}"></el-table-column>
      </#if>
    </#list>
        <el-table-column label="操作">
          <template #default="{ row }: { row: ${table.className}Vo }">
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

    <#-- form对话框 -->
    <${table.className}Form
      ref="formDialog"
      v-model:loading="loading"
      @refresh="refresh({ params: { ...pageParams } })"
    <#list dictList as dict>
      :${dict.javaField}DictData="${dict.javaField}DictData"
    </#list>
    ></${table.className}Form>
  </div>
</template>

<style lang="scss" scoped></style>
