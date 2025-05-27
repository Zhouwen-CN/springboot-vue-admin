<script lang="ts" setup>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import useUserStore from '@/stores/user'
import type { MenuVo } from '@/api/auth/menu'
import { type MenuForm, reqDeleteMenu, reqSaveMenu } from '@/api/auth/menu'
import { ElMessage, type FormInstance, type FormRules, type PopoverInstance } from 'element-plus'

const userStore = useUserStore()
const router = useRouter()

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
  filePath: undefined,
  icon: '',
  keepAlive: false,
  pid: 0
})

// 保存菜单按钮loading
const saveLoading = ref(false)

// 是否存在子节点
const hasChildren = ref(false)

// 表单校验
const ruleFormRef = ref<FormInstance>()
const validateAccessPath = (rule: any, value: any, callback: any) => {
  if (value.trim() === '') {
    callback(new Error('请输入访问路径'))
  } else {
    if ((value as string).startsWith('/')) {
      callback()
    } else {
      callback(new Error('访问路径须以 / 开头'))
    }
  }
}
const validateFilePath = (rule: any, value: any, callback: any) => {
  if (!value || value.startsWith('/')) {
    callback()
  } else {
    callback(new Error('文件路径须以 / 开头'))
  }
}
const rules = reactive<FormRules<typeof menuForm>>({
  title: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' },
    { min: 4, max: 15, message: '长度在 4 到 15 个字符', trigger: 'blur' }
  ],
  accessPath: [{ validator: validateAccessPath, trigger: 'blur' }],
  filePath: [{ validator: validateFilePath, trigger: 'blur' }],
  icon: [{ required: true, message: '请输入菜单图标', trigger: 'submit' }]
})

// 添加主菜单
function addMainMenu() {
  toggleDialog.show = true
  toggleDialog.title = '添加主菜单'
}

// 添加子菜单
function addSubmenu(pid: number) {
  toggleDialog.show = true
  toggleDialog.title = '添加子菜单'
  menuForm.pid = pid
}

// 更新菜单
function updateMenu(row: MenuVo) {
  toggleDialog.show = true
  toggleDialog.title = '更新菜单'
  menuForm.id = row.id
  menuForm.title = row.title
  menuForm.accessPath = row.accessPath
  menuForm.filePath = row.filePath
  menuForm.icon = row.icon
  menuForm.keepAlive = row.keepAlive
  menuForm.pid = row.pid
  hasChildren.value = row.children.length > 0
}

// 删除菜单
async function deleteMenu(menu: MenuVo) {
  await reqDeleteMenu(menu.id)
  await userStore.getMenuInfo()
  router.removeRoute(menu.accessPath)
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
  menuForm.filePath = undefined
  menuForm.icon = ''
  menuForm.keepAlive = false
  menuForm.pid = 0
  hasChildren.value = false
  ruleFormRef.value?.clearValidate()
}

// 搜索图标
const searchIconKeyword = ref('')
// 弹出框
const iconPopoverRef = ref<PopoverInstance>()
// element图标列表
const elementIcons = ref<string[]>(Object.keys(ElementPlusIconsVue))
// 筛选图标
const filteredIcons = computed(() => {
  const trimed = searchIconKeyword.value.trim()
  if (trimed) {
    return elementIcons.value.filter((icon) => icon.toLowerCase().includes(trimed.toLowerCase()))
  } else {
    return elementIcons.value
  }
})

// 选择图标
function selectIcon(icon: string) {
  menuForm.icon = icon
  iconPopoverRef.value?.hide()
}
</script>
<template>
  <div>
    <!-- 表格上面的按钮 -->
    <el-card>
      <div>
        <el-button :icon="Plus" type="primary" @click="addMainMenu">新建 </el-button>
      </div>
      <!-- 表格 -->
      <el-table
        :data="userStore.menuInfo"
        default-expand-all
        row-key="id"
        :border="true"
        show-overflow-tooltip
        style="margin-top: 16px"
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
        <el-table-column label="文件路径" prop="filePath" />
        <el-table-column align="center" label="是否缓存" min-width="40px" prop="keepAlive">
          <template #default="{ row }: { row: MenuVo }">
            <el-switch
              v-model="row.keepAlive"
              active-icon="Check"
              disabled
              inactive-icon="Close"
              inline-prompt
            />
          </template>
        </el-table-column>
        <el-table-column label="更新时间" prop="updateTime" />
        <el-table-column label="操作">
          <template #default="{ row }: { row: MenuVo }">
            <el-button-group>
              <el-button :icon="Plus" type="primary" @click="addSubmenu(row.id)" />
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
    </el-card>

    <!-- 对话框 -->
    <el-dialog v-model="toggleDialog.show" :title="toggleDialog.title" width="40%" @close="clean">
      <template #footer>
        <el-form
          ref="ruleFormRef"
          :model="menuForm"
          :rules="rules"
          label-width="80px"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(ruleFormRef)"
        >
          <el-form-item label="菜单名称" prop="title">
            <el-input v-model="menuForm.title" placeholder="菜单名称"></el-input>
          </el-form-item>
          <el-form-item label="访问路径" prop="accessPath">
            <el-input v-model="menuForm.accessPath" placeholder="访问路径"></el-input>
          </el-form-item>
          <el-form-item label="文件路径" prop="filePath">
            <el-input
              v-model="menuForm.filePath"
              :disabled="hasChildren || menuForm.pid === 0"
              placeholder="文件路径"
            ></el-input>
          </el-form-item>
          <el-form-item label="菜单图标" prop="icon">
            <el-popover ref="iconPopoverRef" :width="400" placement="bottom-start" trigger="click">
              <template #reference>
                <el-input v-model="menuForm.icon" clearable placeholder="菜单图标"> </el-input>
              </template>
              <el-input
                v-model="searchIconKeyword"
                clearable
                placeholder="搜索图标"
                style="margin-bottom: 10px"
              ></el-input>
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
          <el-form-item label="是否缓存" prop="keepAlive">
            <el-switch
              v-model="menuForm.keepAlive"
              :disabled="hasChildren || menuForm.pid === 0"
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
