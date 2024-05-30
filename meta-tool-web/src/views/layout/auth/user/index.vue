<script lang="ts" setup>
import {Delete, Edit, Search} from '@element-plus/icons-vue'
import {computed, onMounted, reactive, ref} from 'vue'
import {
  reqDeleteUser,
  reqDeleteUsers,
  reqGetUserRolePage,
  reqSaveUserRole,
  type UserRoleForm,
  type UserRoleInfo
} from '@/api/auth/user'
import {ElMessage, type FormInstance, type FormRules} from 'element-plus'
import {reqGetRoles} from '@/api/auth/role'
import useUserStore from '@/stores/user'
import {useRouter} from 'vue-router'
import {deleteAsyncRoutesAndExit} from '@/router/asyncRoutes'

const userStore = useUserStore()
const router = useRouter()

// 表单提交对象
const userRoleForm = reactive<UserRoleForm>({
  id: undefined,
  username: '',
  password: '',
  roleIds: []
})
const ruleFormRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof userRoleForm>>({
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 5, max: 10, message: '长度在 5 到 10 个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 5, message: '长度应大于5位', trigger: 'blur'}
  ]
})

// 对话框切换
const toggleDialog = reactive({
  show: false,
  title: ''
})

// 获取分页数据
const searchName = ref('')
const {
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
// 获取角色列表
const {data: roleData, refresh: roleRefresh} = reqGetRoles()
const handleCheckAllChange = (val: boolean) => {
  if (val) {
    userRoleForm.roleIds = roleData.value?.map((role) => role.id) || []
  } else {
    userRoleForm.roleIds = []
  }
  isIndeterminate.value = false
}
const handleCheckedCitiesChange = (value: string[]) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === roleData.value?.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < (roleData.value?.length as number)
}

// 添加用户
function addUser() {
  toggleDialog.show = true
  toggleDialog.title = '添加用户'
}

// 更新用户
function updateUser(row: UserRoleInfo) {
  toggleDialog.show = true
  toggleDialog.title = '修改用户'
  userRoleForm.id = row.id
  userRoleForm.username = row.username
  userRoleForm.password = row.password
  userRoleForm.roleIds = row.roleIds?.split(',').map((id) => Number(id)) || []
}

// 删除用户
async function deleteUser(id: number) {
  try {
    await reqDeleteUser(id)
    pageRefresh()
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  }
}

// 批量删除
const deleteIds = ref<number[]>([])
function handleSelectionChange(users: UserRoleInfo[]) {
  deleteIds.value = users.map((user) => user.id)
}
async function deleteUsers() {
  if (deleteIds.value.length === 0) {
    ElMessage.warning('请选择要删除的用户')
    return
  }

  try {
    await reqDeleteUsers(deleteIds.value)
    pageRefresh()
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  }
}

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  try {
    await formEl.validate()
    await reqSaveUserRole(userRoleForm)
    toggleDialog.show = false
    ElMessage.success('操作成功')

    // 如果修改的是当前用户，则退出重新登入
    if (userRoleForm.username === userStore.userMenuInfo.username) {
      deleteAsyncRoutesAndExit(router, userStore)
      return
    }

    pageRefresh()
  } catch (error) {
    // do nothing
  }
}

// 对话框关闭时清空数据 和 错误提示样式
function clean() {
  toggleDialog.title = ''
  userRoleForm.id = undefined
  userRoleForm.username = ''
  userRoleForm.password = ''
  userRoleForm.roleIds = []
  checkAll.value = false
  isIndeterminate.value = true
  ruleFormRef.value?.clearValidate()
}

// 根据角色id列表 获取角色名称列表
const getRoleNames = computed(() => (roleIds: string) => {
  const ids = roleIds?.split(',').map((id) => Number(id)) || []
  const result: string[] = []
  roleData.value?.forEach((role) => {
    if (ids.includes(role.id)) {
      result.push(role.roleName)
    }
  })
  return result.join(',')
})

onMounted(() => {
  pageRefresh()
  roleRefresh()
})
</script>

<template>
  <div>
    <!-- 顶部搜索框 -->
    <el-card>
      <el-form inline>
        <el-form-item label="用户名：">
          <el-input v-model="searchName" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" type="primary" @click="pageRefresh({ params: { searchName } })"
          >搜索
          </el-button
          >
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 16px">
      <!-- 表格上面的按钮 -->
      <div>
        <el-button type="primary" @click="addUser">添加用户</el-button>
        <el-popconfirm title="是否删除？" @confirm="deleteUsers">
          <template #reference>
            <el-button type="danger">批量删除</el-button>
          </template>
        </el-popconfirm>
      </div>

      <!-- 表格 -->
      <el-table
          :border="true"
          :data="pageData"
          row-key="id"
          style="margin-top: 16px"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column label="ID" prop="id"></el-table-column>
        <el-table-column label="用户名称" prop="username"></el-table-column>
        <el-table-column label="角色名称" prop="roleIds">
          <template #default="{ row }">
            {{ getRoleNames(row.roleIds) }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button-group>
              <el-button :icon="Edit" type="primary" @click="updateUser(row)"></el-button>
              <el-popconfirm title="是否删除？" @confirm="deleteUser(row.id)">
                <template #reference>
                  <el-button :disabled="row.id === 1" :icon="Delete" type="danger"></el-button>
                </template>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <!-- TODO: jumper有bug，会出现警告信息，暂时不用 -->
      <el-pagination
          v-model:current-page="current"
          v-model:page-size="size"
          :page-sizes="sizeOption"
          :total="total"
          background
          layout="prev, pager, next, ->, total, sizes"
          style="margin-top: 16px"
          @current-change="onPageChange"
          @size-change="onSizeChange"
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
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userRoleForm.username" placeholder="请输入用户姓名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="userRoleForm.password"
                placeholder="请输入用户密码"
                type="password"
            ></el-input>
          </el-form-item>
          <!-- 多选框组 -->
          <el-form-item label="角色列表" prop="roleIds">
            <el-space v-if="userRoleForm.id !== 1" alignment="stretch" direction="vertical">
              <el-checkbox
                  v-model="checkAll"
                  :indeterminate="isIndeterminate"
                  @change="handleCheckAllChange"
              >
                全选
              </el-checkbox>
              <!-- TODO: 不能授予用户admin角色 -->
              <el-checkbox-group v-model="userRoleForm.roleIds" @change="handleCheckedCitiesChange">
                <el-checkbox
                    v-for="role in roleData"
                    :key="role.id"
                    :disabled="role.roleName === 'admin'"
                    :label="role.roleName"
                    :value="role.id"
                >
                  {{ role.roleName }}
                </el-checkbox>
              </el-checkbox-group>
            </el-space>
          </el-form-item>
          <el-form-item>
            <el-button @click="toggleDialog.show = false">取消</el-button>
            <el-button type="primary" @click="onSubmit(ruleFormRef)">确认</el-button>
          </el-form-item>
        </el-form>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped></style>
