<script lang="ts" setup>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'
import type { MenuVo } from '@/api/auth/menu'
import { type MenuForm, reqDeleteMenu, reqSaveMenu } from '@/api/auth/menu'
import { ElMessage, type FormInstance, type FormRules, type PopoverInstance } from 'element-plus'
import useDict from '@/hooks/useDictionary'
import MenuTreeSelect from '@/components/MenuTreeSelect.vue'
import useAppStore from '@/stores/app'
const appStore = useAppStore()

const userStore = useUserStore()

// 获取字典
const { dictData, run } = useDict(1, false)

// 对话框
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 表单数据
const menuForm = reactive<MenuForm>({
  id: undefined,
  title: '',
  accessPath: '',
  icon: '',
  keepAlive: false,
  pid: 0,
  menuType: 0,
  sortId: 0
})

// 保存菜单按钮loading
const saveLoading = ref(false)

// 表单校验
const ruleFormRef = ref<FormInstance>()
const validateAccessPath = (rule: any, value: any, callback: any) => {
  if (value && value.startsWith('/')) {
    callback()
  } else {
    callback(new Error('访问路径须以 / 开头'))
  }
}

const rules = reactive<FormRules<typeof menuForm>>({
  title: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' },
    { max: 15, message: '菜单名称长度不能大于15', trigger: 'blur' }
  ],
  accessPath: [{ validator: validateAccessPath, trigger: 'blur' }],
  icon: [{ required: true, message: '请输入菜单图标', trigger: 'submit' }]
})

// 添加菜单
function addMenu() {
  toggleDialog.show = true
  toggleDialog.title = '添加菜单'
}

// 更新菜单
function updateMenu(row: MenuVo) {
  toggleDialog.show = true
  toggleDialog.title = '更新菜单'
  menuForm.id = row.id
  menuForm.title = row.title
  menuForm.accessPath = row.accessPath
  menuForm.icon = row.icon
  menuForm.keepAlive = row.keepAlive
  menuForm.pid = row.pid
  menuForm.menuType = row.menuType
  menuForm.sortId = row.sortId
}

// 删除菜单
async function deleteMenu(menu: MenuVo) {
  await reqDeleteMenu(menu.id)
  await userStore.getMenuInfo()
  ElMessage.success('操作成功')
}

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqSaveMenu(menuForm)
    // 重新请求表单信息
    await userStore.getMenuInfo()
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 打开对话框前的清理操作
function clean() {
  toggleDialog.title = ''
  menuForm.id = undefined
  menuForm.title = ''
  menuForm.accessPath = ''
  menuForm.icon = ''
  menuForm.keepAlive = false
  menuForm.pid = 0
  menuForm.menuType = 0
  menuForm.sortId = 0
  ruleFormRef.value?.clearValidate()
  // 关闭图标选择器
  iconPopoverRef.value?.hide()
}

// 弹出框
const iconPopoverRef = ref<PopoverInstance>()
// element图标列表
const elementIcons = Object.keys(ElementPlusIconsVue)
// 筛选图标
const filteredIcons = ref<string[]>(elementIcons)

function searchIcon(value: string) {
  const keyword = value.trim()
  if (keyword) {
    filteredIcons.value = elementIcons.filter((icon) =>
      icon.toLowerCase().includes(keyword.toLowerCase())
    )
  } else {
    filteredIcons.value = elementIcons
  }
}

// 选择图标
function selectIcon(icon: string) {
  menuForm.icon = icon
  iconPopoverRef.value?.hide()
}

onMounted(() => {
  run()
})
</script>
<template>
  <div>
    <!-- 表格上面的按钮 -->
    <el-card>
      <div>
        <el-button :icon="Plus" type="primary" @click="addMenu">新建</el-button>
      </div>
      <!-- 表格 -->
      <div style="margin-top: 16px">
        <el-table
          :data="userStore.menuInfo"
          default-expand-all
          row-key="id"
          :border="true"
          show-overflow-tooltip
        >
          <el-table-column label="菜单名称" prop="title">
            <template #default="{ row }: { row: MenuVo }">
              <el-space>
                <el-icon :size="20">
                  <component :is="row.icon"></component>
                </el-icon>
                <span>{{ row.title }}</span>
              </el-space>
            </template>
          </el-table-column>
          <el-table-column label="访问路径" prop="accessPath" />
          <el-table-column label="文件路径" prop="filePath">
            <template #default="{ row }: { row: MenuVo }">
              {{ row.children.length > 0 ? '' : `${row.accessPath}/index.vue` }}
            </template>
          </el-table-column>
          <el-table-column label="是否缓存" prop="keepAlive">
            <template #default="{ row }: { row: MenuVo }">
              <el-text :type="row.keepAlive ? 'success' : 'danger'">{{
                row.keepAlive ? '启用' : '禁用'
              }}</el-text>
            </template>
          </el-table-column>
          <el-table-column label="菜单排序" prop="sortId"></el-table-column>
          <el-table-column label="更新时间" prop="updateTime" />
          <el-table-column label="操作">
            <template #default="{ row }: { row: MenuVo }">
              <el-button-group>
                <el-button :icon="Edit" type="primary" @click="updateMenu(row)" />
                <el-popconfirm title="是否删除？" @confirm="deleteMenu(row)">
                  <template #reference>
                    <el-button :disabled="row.children.length > 0" :icon="Delete" type="danger" />
                  </template>
                </el-popconfirm>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!-- 对话框 -->
    <el-dialog
      v-model="toggleDialog.show"
      :title="toggleDialog.title"
      :width="appStore.device === 'desktop' ? '50%' : '80%'"
      :align-center="appStore.device!=='desktop'"
      @close="clean"
    >
      <template #footer>
        <el-form
          ref="ruleFormRef"
          :model="menuForm"
          :rules="rules"
          label-width="auto"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(ruleFormRef)"
        >
          <el-form-item label="上级菜单" prop="pid">
            <MenuTreeSelect v-model="menuForm.pid" />
          </el-form-item>
          <el-form-item label="菜单名称" prop="title">
            <el-input v-model="menuForm.title" placeholder="菜单名称"></el-input>
          </el-form-item>
          <el-form-item label="菜单类型" prop="menuType">
            <el-radio-group v-model="menuForm.menuType">
              <el-radio-button
                v-for="dict in dictData"
                :label="dict.label"
                :value="dict.data"
                :key="dict.data"
              />
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-tip="`访问路径 + /index.vue = 文件路径`"
            label="访问路径"
            prop="accessPath"
          >
            <el-input v-model="menuForm.accessPath" placeholder="访问路径"></el-input>
          </el-form-item>
          <el-form-item v-tip="`数值越小越靠前`" label="菜单排序" prop="sortId">
            <el-input v-model="menuForm.sortId" type="number"></el-input>
          </el-form-item>
          <el-form-item label="菜单图标" prop="icon">
            <el-popover ref="iconPopoverRef" :width="400" placement="bottom-start" trigger="click">
              <template #reference>
                <el-input
                  v-model="menuForm.icon"
                  clearable
                  placeholder="菜单图标"
                  @input="searchIcon"
                >
                </el-input>
              </template>
              <el-scrollbar always height="300px">
                <ul class="icon-grid">
                  <li
                    v-for="icon in filteredIcons"
                    :key="icon"
                    class="icon-grid-item"
                    @click="selectIcon(icon)"
                  >
                    <el-icon :size="20">
                      <component :is="icon" />
                    </el-icon>
                  </li>
                </ul>
              </el-scrollbar>
            </el-popover>
          </el-form-item>
          <el-form-item label="是否缓存" prop="keepAlive" v-if="menuForm.menuType">
            <el-switch
              v-model="menuForm.keepAlive"
              active-icon="Check"
              inactive-icon="Close"
              inline-prompt
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消 </el-button>
            <el-button :loading="saveLoading" type="primary" native-type="submit"> 确认 </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.icon-grid {
  display: flex;
  flex-wrap: wrap;

  .icon-grid-item {
    padding: 8px;
    margin: 4px;
    cursor: pointer;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    transition: all 0.3s;

    &:hover {
      border-color: #4080ff;
      transform: scale(1.2);
    }
  }
}
</style>
