<script lang="ts" setup>
import { Lock, User } from '@element-plus/icons-vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import type { LoginForm } from '@/api/auth/user'
import useUserStore from '@/stores/user'
import useSettingStore from '@/stores/setting'

const router = useRouter()
const userStore = useUserStore()
const settingStore = useSettingStore()
const loading = ref<boolean>(false)
const ruleFormRef = ref<FormInstance>()
const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

// 登入表单校验
const rules = reactive<FormRules<typeof loginForm>>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 15, message: '长度在 5 到 15 个字符', trigger: 'blur' }
  ]
})

// 表单提交
const onSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  loading.value = true
  try {
    await formEl.validate()
    await userStore.doLogin(loginForm)
    await userStore.getMenuInfo()

    let redirect = router.currentRoute.value.query.redirect
    if (!redirect) {
      redirect = '/'
    }
    await router.replace(redirect as string)
    ElMessage.success('登入成功')
  } catch (error) {
    // do nothing
  } finally {
    loading.value = false
  }
}

// 表单重置
const onCancel = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<template>
  <el-row class="container">
    <el-col :span="13"></el-col>
    <el-col :span="8" class="content">
      <el-form
        ref="ruleFormRef"
        :model="loginForm"
        :rules="rules"
        @submit.prevent="onSubmit(ruleFormRef)"
        class="form"
        size="large"
        label-width="auto"
      >
        <h1>{{ settingStore.appName }}</h1>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            :prefix-icon="User"
            placeholder="用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            :prefix-icon="Lock"
            placeholder="密码"
            show-password
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" type="primary" native-type="submit">登录 </el-button>
          <el-button @click="onCancel(ruleFormRef)">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="3"></el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: 100vh;
  background-size: cover;
  background-image: url('@/assets/images/login_background.jpg');
  background-repeat: no-repeat;
  background-position: center;

  .content {
    display: flex;
    justify-content: center;
    align-items: center;

    .form {
      width: 100%;
      padding: 50px 50px 20px 50px;
      border-radius: 5%;
      background-size: cover;
      background-image: url('@/assets/images/login_form.png');
      background-repeat: no-repeat;
      background-position: center;
    }

    h1 {
      font-size: 36px;
      color: white;
      margin-bottom: 20px;
    }
  }
}
</style>
