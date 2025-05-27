<script lang="ts" setup>
import { Delete, Edit, Plus, Refresh, Search } from '@element-plus/icons-vue'
import {
  reqDeleteUser,
  reqDeleteUsers,
  reqGetUserRolePage,
  reqResetPassword,
  reqSaveUserRole,
  type UserRoleForm,
  type UserRoleVo
} from '@/api/auth/user'
import type { CheckboxValueType } from 'element-plus'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { reqGetRoles, type RoleVo } from '@/api/auth/role'
import useUserStore from '@/stores/user'
import { deleteAsyncRoutes } from '@/router/asyncRoutes'
import useTagViewStore from '@/stores/tagView'
import useSettingStore from '@/stores/setting'

const userStore = useUserStore()
const router = useRouter()

// 表单数据
const userRoleForm = reactive<UserRoleForm>({
  id: undefined,
  username: '',
  password: undefined,
  roleIds: []
})

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 显示隐藏密码输入框
const pwdVisible = ref(false)

// 保存角色按钮loading
const saveLoading = ref(false)

// 角色列表数据
const roleData = ref<RoleVo[]>([])

// 获取分页数据
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
} = reqGetUserRolePage()

// 多选框相关
const checkAll = ref(false)
const isIndeterminate = ref(true)

const handleCheckAllChange = (val: CheckboxValueType) => {
  if (val) {
    userRoleForm.roleIds = roleData.value?.map((role) => role.id) || []
  } else {
    userRoleForm.roleIds = []
  }
  isIndeterminate.value = false
}
const handleCheckedCitiesChange = (value: CheckboxValueType[]) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === roleData.value?.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < (roleData.value?.length as number)
}

// 查询用户
function searchUser() {
  searchName.value = searchName.value.trim()
  pageRefresh({ params: { searchName: searchName.value } })
}

// 添加用户
function addUser() {
  toggleDialog.show = true
  toggleDialog.title = '添加用户'
  pwdVisible.value = true
}

// 更新用户
function updateUser(row: UserRoleVo) {
  toggleDialog.show = true
  toggleDialog.title = '修改用户'
  userRoleForm.id = row.id
  userRoleForm.username = row.username
  userRoleForm.roleIds = row.roleList.map((role) => role.id)
  pwdVisible.value = false
}

// 重置密码
async function resetPassword(id: number) {
  await reqResetPassword(id)
  if (isCurrentUser(id)) {
    return
  }
  ElMessage.success('操作成功')
}

// 删除用户
async function deleteUser(id: number) {
  await reqDeleteUser(id)
  pageRefresh({ params: { searchName: searchName.value } })
  ElMessage.success('操作成功')
}

// 批量删除
const deleteIds = ref<number[]>([])
function handleSelectionChange(users: UserRoleVo[]) {
  deleteIds.value = users.map((user) => user.id)
}
async function deleteUsers() {
  if (deleteIds.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  await reqDeleteUsers(deleteIds.value)
  pageRefresh({ params: { searchName: searchName.value } })
  ElMessage.success('操作成功')
}

// 表单校验
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof userRoleForm>>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, message: '长度不能小于 5 个字符', trigger: 'blur' }
  ]
})

// 修改的是否当前用户，是则需要重新登入
function isCurrentUser(updateId: number) {
  const isCurUser = updateId === userStore.userInfo.id
  if (isCurUser) {
    userStore.$reset()
    useTagViewStore().$reset()
    useSettingStore().$reset()
    deleteAsyncRoutes(router)
    ElMessage.warning('修改成功，请重新登入')
  }
  return isCurUser
}

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqSaveUserRole(userRoleForm)
    toggleDialog.show = false

    // 如果修改的是当前用户，则退出重新登入
    if (isCurrentUser(userRoleForm.id as number)) {
      return
    }

    ElMessage.success('操作成功')
    pageRefresh({ params: { searchName: searchName.value } })
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 对话框关闭时清空数据 和 错误提示样式
function clean() {
  toggleDialog.title = ''
  userRoleForm.id = undefined
  userRoleForm.username = ''
  userRoleForm.password = undefined
  userRoleForm.roleIds = []
  checkAll.value = false
  isIndeterminate.value = true
  ruleFormRef.value?.clearValidate()
}

onMounted(() => {
  // 这里不需要同步
  reqGetRoles().then((res) => (roleData.value = res.data))
  pageRefresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline @submit.prevent="searchUser()">
        <el-form-item label="用户名">
          <el-input v-model="searchName" clearable placeholder="用户名"></el-input>
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
        <el-button :icon="Plus" type="primary" @click="addUser">新建 </el-button>
        <el-popconfirm title="是否删除？" @confirm="deleteUsers">
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
        <el-table-column label="ID" prop="id"></el-table-column>
        <el-table-column label="用户名称" prop="username"></el-table-column>
        <el-table-column label="角色名称" prop="roleIds">
          <template #default="{ row }: { row: UserRoleVo }">
            {{ row.roleList.map((role) => role.roleName).join(',') }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }: { row: UserRoleVo }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="updateUser(row)"></el-button>
              <el-popconfirm title="是否重置密码？" @confirm="resetPassword(row.id)">
                <template #reference>
                  <el-button :icon="Refresh" type="warning"></el-button>
                </template>
              </el-popconfirm>
              <el-popconfirm title="是否删除？" @confirm="deleteUser(row.id)">
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
          :model="userRoleForm"
          :rules="rules"
          label-width="80px"
          style="padding: 0 20px"
          @submit.prevent="onSubmit(ruleFormRef)"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userRoleForm.username" placeholder="用户名"></el-input>
          </el-form-item>
          <el-form-item v-if="pwdVisible" label="密码" prop="password">
            <el-input v-model="userRoleForm.password" placeholder="密码" type="password">
            </el-input>
          </el-form-item>
          <!-- 多选框组 -->
          <el-form-item label="角色列表" prop="roleIds">
            <el-space alignment="stretch" direction="vertical">
              <el-checkbox
                v-model="checkAll"
                :indeterminate="isIndeterminate"
                @change="handleCheckAllChange"
              >
                全选
              </el-checkbox>
              <el-checkbox-group v-model="userRoleForm.roleIds" @change="handleCheckedCitiesChange">
                <el-checkbox
                  v-for="role in roleData"
                  :key="role.id"
                  :label="role.roleName"
                  :value="role.id"
                >
                  {{ role.roleName }}
                </el-checkbox>
              </el-checkbox-group>
            </el-space>
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消 </el-button>
            <el-button :loading="saveLoading" type="primary" native-type="submit">确认 </el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.header-row-style {
  font-weight: 700;
}
</style>
