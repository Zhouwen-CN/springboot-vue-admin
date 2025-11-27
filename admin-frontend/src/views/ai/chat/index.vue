<script lang="ts" setup>
import type {
  ConversationItem,
  ConversationMenuCommand
} from 'vue-element-plus-x/types/Conversations'
import {
  type ChatConversationRenameForm,
  type ChatHistoryVo,
  reqDeleteChatConversationById,
  reqGetChatHistory,
  reqRenameChatConversationTitle
} from '@/api/ai/chat'
import { Conversations } from 'vue-element-plus-x'
import type { FormInstance, FormRules } from 'element-plus'
import ConversationList from './components/ConversationList.vue'
import { Plus } from '@element-plus/icons-vue'
import useAppStore from '@/stores/app'
const appStore = useAppStore()

// 聊条历史
const chatHistoryVo = ref<ConversationItem<ChatHistoryVo>[]>([])
// 会话id
const chatId = ref<string>()
// 表单对话框显示状态
const toggleDialog = ref(false)
// 聊天会话重命名表单
const form = reactive<ChatConversationRenameForm>({
  conversationId: '',
  title: ''
})
// 表单提交loading
const loading = ref(false)

// 获取聊天历史
async function getChatHistory() {
  const result = await reqGetChatHistory()
  chatHistoryVo.value = result.data
}

// 删除编辑按钮
async function handleMenuCommand(
  command: ConversationMenuCommand,
  item: ConversationItem<ChatHistoryVo>
) {
  const commandString = command.toString()
  if (commandString === 'rename') {
    form.conversationId = item.conversationId
    form.title = item.title
    toggleDialog.value = true
  } else if (commandString === 'delete') {
    await deleteChatConversationById(item)
  }
}

// 删除聊天会话
async function deleteChatConversationById(item: ConversationItem<ChatHistoryVo>) {
  const conversationId = item.conversationId
  if (chatId.value === conversationId) {
    chatId.value = undefined
  }
  await reqDeleteChatConversationById(conversationId)
  await getChatHistory()
  ElMessage.success('删除成功')
}

// 表单提交
const formRef = ref<FormInstance>()
const rules = reactive<FormRules<typeof form>>({
  title: [{ required: true, message: '请输入会话名称', trigger: 'blur' }]
})

async function onSubmit(formEl: FormInstance | undefined) {
  if (!formEl) return
  loading.value = true
  try {
    await formEl.validate()
    await reqRenameChatConversationTitle(form)
    await getChatHistory()
    toggleDialog.value = false
    ElMessage.success('操作成功')
  } catch (error) {
    // do nothing
  } finally {
    loading.value = false
  }
}

async function refreshConversations() {
  await getChatHistory()
}

function clean() {
  form.conversationId = ''
  form.title = ''
}

onMounted(() => {
  getChatHistory()
})
</script>

<template>
  <div>
    <div class="container">
      <div class="sidebar">
        <Conversations
          v-model:active="chatId"
          :items="chatHistoryVo"
          :label-max-width="260"
          labelKey="title"
          row-key="conversationId"
          show-tooltip
          showBuiltInMenu
          showToTopBtn
          :style="{ width: '260px', display: appStore.device === 'desktop' ? '' : 'none' }"
          @menuCommand="handleMenuCommand"
        >
          <template #header>
            <el-button
              :icon="Plus"
              plain
              round
              style="margin: 0 20px"
              type="primary"
              :style="{ display: appStore.device === 'desktop' ? 'block' : 'none' }"
              @click="chatId = undefined"
            >
              新建会话
            </el-button>
          </template>
        </Conversations>
      </div>
      <div class="content">
        <ConversationList v-model="chatId" @refresh="refreshConversations"> </ConversationList>
      </div>
    </div>
    <el-dialog
      v-model="toggleDialog"
      title="编辑对话名称"
      :width="appStore.device === 'desktop' ? '50%' : '80%'"
      :align-center="appStore.device !== 'desktop'"
      @close="clean"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="auto"
        style="padding: 0 20px"
        @submit.prevent="onSubmit(formRef)"
      >
        <el-form-item prop="title">
          <el-input
            v-model="form.title"
            :autosize="{ minRows: 3, maxRows: 6 }"
            placeholder="任务名称"
            type="textarea"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="toggleDialog = false">取消</el-button>
          <el-button native-type="submit" type="primary">确认</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.container {
  display: flex;

  .sidebar {
    height: calc(100vh - $base_header_height - 40px);
  }

  .content {
    height: calc(100vh - $base_header_height - 40px);
    flex: 1;
  }
}
</style>
