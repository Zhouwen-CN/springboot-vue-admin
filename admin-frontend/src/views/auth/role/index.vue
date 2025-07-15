<script lang="ts" setup>
import {Delete, Edit, Plus, Search} from '@element-plus/icons-vue'
import {
  reqDeleteRole,
  reqDeleteRoles,
  reqGetRolePage,
  reqSaveRoleMenu,
  type RoleMenuForm,
  type RoleMenuVo
} from '@/api/auth/role'
import {ElMessage, type FormInstance, type FormRules, type TreeInstance} from 'element-plus'
import useUserStore from '@/stores/user'
import type {MenuVo} from '@/api/auth/menu'

const userStore = useUserStore()

// 表单数据
const roleMenuForm = reactive<RoleMenuForm>({
  id: undefined,
  roleName: '',
  desc: '',
  menuIds: []
})

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 保存角色按钮loading
const saveLoading = ref(false)

// 分页
const searchName = ref('')
const {
  loading,
  current,
  total,
  size,
  sizeOption,
  data: pageData,
  refresh: pageRefresh,
  onPageChange,
  onSizeChange
} = reqGetRolePage()

// 查询角色
function searchRole() {
  searchName.value = searchName.value.trim()
  pageRefresh({params: {searchName: searchName.value}})
}

// 添加角色
function addRole() {
  toggleDialog.show = true
  toggleDialog.title = '添加角色'
}

// 更新角色
function updateRole(row: RoleMenuVo) {
  toggleDialog.show = true
  toggleDialog.title = '修改角色'
  roleMenuForm.id = row.id
  roleMenuForm.roleName = row.roleName
  roleMenuForm.desc = row.desc
  const menuIds = row.menuIds
  roleMenuForm.menuIds = getSelectKeys(userStore.menuInfo as MenuVo[], menuIds)
}

// 删除角色
async function deleteRole(id: number) {
  await reqDeleteRole(id)
  pageRefresh({params: {searchName: searchName.value}})
  ElMessage.success('操作成功')
}

// 批量删除
const deleteIds = ref<number[]>([])

function handleSelectionChange(roles: RoleMenuVo[]) {
  deleteIds.value = roles.map((role) => role.id)
}

async function deleteRoles() {
  if (deleteIds.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  await reqDeleteRoles(deleteIds.value)
  pageRefresh({params: {searchName: searchName.value}})
  ElMessage.success('操作成功')
}

// 表单校验
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof roleMenuForm>>({
  roleName: [
    {required: true, message: '请输入角色名称', trigger: 'blur'},
    {min: 3, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur'}
  ],
  desc: [
    {required: true, message: '角色说明', trigger: 'blur'},
    {min: 1, max: 40, message: '长度不能大于 40 个字符', trigger: 'blur'}
  ]
})

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    // 选中的 和 半选中的菜单
    const checkedKeys = menuTreeRef.value?.getCheckedKeys() || []
    const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() || []
    roleMenuForm.menuIds = checkedKeys.concat(halfCheckedKeys).map((key) => Number(key))
    await reqSaveRoleMenu(roleMenuForm)
    pageRefresh({params: {searchName: searchName.value}})
    toggleDialog.show = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 树形控件
const menuTreeRef = ref<TreeInstance>()
const defaultProps = {
  children: 'children',
  label: 'title'
}

/**
 * 获取树形控件选中项，递归遍历菜单，剔除非叶子节点
 * @param menus 菜单列表
 * @param menuIds 当前需要选中的id列表（包含非叶子节点）
 * @param selectedKeys
 */
function getSelectKeys(menus: MenuVo[], menuIds: number[], selectedKeys: number[] = []): number[] {
  menus.forEach((menu) => {
    if ((!menu.children || menu.children.length === 0) && menuIds.includes(menu.id)) {
      selectedKeys.push(menu.id)
    } else {
      getSelectKeys(menu.children, menuIds, selectedKeys)
    }
  })
  return selectedKeys
}

// 对话框关闭时清空数据 和 错误提示样式
function clean() {
  toggleDialog.title = ''
  roleMenuForm.id = undefined
  roleMenuForm.roleName = ''
  roleMenuForm.desc = ''
  roleMenuForm.menuIds = []
  ruleFormRef.value?.clearValidate()
  menuTreeRef.value?.setCheckedKeys([])
}

onMounted(() => {
  pageRefresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline @submit.prevent="searchRole()" label-width="auto">
        <el-form-item label="角色名">
          <el-input v-model="searchName" clearable placeholder="角色名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" :loading="loading" type="primary" native-type="submit"
          >搜索
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格上面的按钮 -->
      <div>
        <el-button :icon="Plus" type="primary" @click="addRole">新建</el-button>
        <el-popconfirm title="是否删除？" @confirm="deleteRoles">
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
        <el-table-column type="selection" width="45"/>
        <el-table-column label="ID" prop="id"></el-table-column>
        <el-table-column label="角色名称" prop="roleName"></el-table-column>
        <el-table-column label="说明" prop="desc"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }: { row: RoleMenuVo }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="updateRole(row)"></el-button>
              <el-popconfirm title="是否删除？" @confirm="deleteRole(row.id)">
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
          @current-change="(val: number) => onPageChange(val, { params: { searchName: searchName } })"
          @size-change="(val: number) => onSizeChange(val, { params: { searchName: searchName } })"
      />
    </el-card>

    <!-- 对话框表单 -->
    <el-dialog v-model="toggleDialog.show" :title="toggleDialog.title" width="40%" @close="clean">
      <template #footer>
        <el-form
            ref="ruleFormRef"
            :model="roleMenuForm"
            :rules="rules"
            label-width="auto"
            style="padding: 0 20px"
            @submit.prevent="onSubmit(ruleFormRef)"
        >
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="roleMenuForm.roleName" placeholder="角色名称"></el-input>
          </el-form-item>
          <el-form-item label="角色说明" prop="desc">
            <el-input v-model="roleMenuForm.desc" placeholder="角色说明"></el-input>
          </el-form-item>
          <!-- 树形控件 -->
          <el-form-item label="菜单权限">
            <el-tree
                ref="menuTreeRef"
                :data="userStore.menuInfo"
                :default-checked-keys="roleMenuForm.menuIds"
                :props="defaultProps"
                node-key="id"
                show-checkbox
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消</el-button>
            <el-button :loading="saveLoading" type="primary" native-type="submit">确认</el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
