<script lang="ts" setup>
import useUserStore from '@/stores/user'
import { ArrowRight, FullScreen } from '@element-plus/icons-vue'
import useSettingStore from '@/stores/setting'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { type ChangePwdForm, reqChangePassword } from '@/api/auth/user'
import { useRouter } from 'vue-router'
import { deleteAsyncRoutes } from '@/router/asyncRoutes'
import useTagViewStore from '@/stores/tagView'
import useAppStore from '@/stores/app'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const tagViewStore = useTagViewStore()
const settingStore = useSettingStore()
const appStore = useAppStore()

// 对话款隐藏/可见
const dialogVisible = ref(false)

// 表单提交loading
const saveLoading = ref(false)

// 表单对象
const changePwdForm = reactive<ChangePwdForm>({
  oldPwd: '',
  newPwd: '',
  confirmPwd: ''
})

// 切换全屏模式
function toggleFullScreen() {
  const isFullScreen = document.fullscreenElement

  if (!isFullScreen) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 修改密码
function changePassword() {
  dialogVisible.value = true
}

// 登出
async function logout() {
  try {
    await userStore.doLogout()
    ElMessage.success('退出成功')
  } catch (error) {
    // do noting
  }
}

// 路由信息
const routeInfo = computed(() => {
  return route.matched.filter((item) => item.meta.title)
})

// 菜单收起/展开
function changeCollapse() {
  if (appStore.device === 'mobile') {
    settingStore.sidebarHidden = !settingStore.sidebarHidden
  } else {
    settingStore.sidebarCollapse = !settingStore.sidebarCollapse
  }
}

// 表单校验
const confirmValidator = (rule: any, value: any, callback: any) => {
  value === changePwdForm.newPwd ? callback() : callback(new Error('两次输入的密码不一致'))
}
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof changePwdForm>>({
  oldPwd: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
  ],
  newPwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请输入确认密码', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' },
    { validator: confirmValidator, trigger: 'submit' }
  ]
})

// 表单提交
async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  saveLoading.value = true
  try {
    await formEl.validate()
    await reqChangePassword(changePwdForm)
    dialogVisible.value = false
    userStore.$reset()
    tagViewStore.$reset()
    settingStore.$reset()
    deleteAsyncRoutes(router)
    ElMessage.info('修改成功，请重新登入')
  } catch (error) {
    // do nothing
  } finally {
    saveLoading.value = false
  }
}

// 对话框关闭时清空数据 和 错误提示样式
function clean() {
  changePwdForm.oldPwd = ''
  changePwdForm.newPwd = ''
  changePwdForm.confirmPwd = ''
  formRef.value?.clearValidate()
}
</script>

<template>
  <div class="container">
    <!-- 展开收起按钮 -->
    <el-icon :size="20" style="margin-right: 10px" @click.stop="changeCollapse">
      <component :is="settingStore.sidebarCollapse ? 'Expand' : 'Fold'"> </component>
    </el-icon>
    <!-- 左侧面包屑 -->
    <el-breadcrumb :separator-icon="ArrowRight" class="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in routeInfo" :key="index" :to="item.path">
        <el-icon>
          <component :is="item.meta.icon"></component>
        </el-icon>
        <span style="margin-left: 5px">{{ item.meta.title }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 右侧头像等图标 -->
    <el-space>
      <el-button :icon="FullScreen" circle size="default" @click="toggleFullScreen"></el-button>

      <el-dropdown>
        <el-avatar :src="settingStore.avatarUrl" shape="square"> </el-avatar>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="changePassword">修改密码 </el-dropdown-item>
            <el-dropdown-item @click="logout">退出登入 </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-space>

    <!-- 对话框表单 -->
    <el-dialog
      v-model="dialogVisible"
      title="修改密码"
      :width="appStore.device === 'desktop' ? '50%' : '80%'"
      :align-center="appStore.device !== 'desktop'"
      body-class="dialog-body"
      @close="clean"
    >
      <el-form
        ref="formRef"
        :model="changePwdForm"
        :rules="rules"
        label-width="auto"
        style="padding: 0 20px"
        @submit.prevent="onSubmit(formRef)"
      >
        <el-form-item label="旧密码" prop="oldPwd">
          <el-input
            v-model="changePwdForm.oldPwd"
            placeholder="旧密码"
            show-password
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input
            v-model="changePwdForm.newPwd"
            placeholder="新密码"
            show-password
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input
            v-model="changePwdForm.confirmPwd"
            placeholder="确认密码"
            show-password
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button :loading="saveLoading" native-type="submit" type="primary">确认 </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.container {
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 0.8px solid var(--el-border-color);

  .breadcrumb {
    flex: 1;
  }

  .dialog-body {
    width: 1000px;
  }
}
</style>
