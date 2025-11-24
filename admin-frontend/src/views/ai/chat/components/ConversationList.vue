<script lang="ts" setup>
import {
  type ChatMessageDto,
  reqAddChatConversation,
  reqGetChatConversationList,
  useChat
} from '@/api/ai/chat'
import type { BubbleListInstance, BubbleListItemProps } from 'vue-element-plus-x/types/BubbleList'
import type { ThinkingStatus } from 'vue-element-plus-x/types/Thinking'
import { BubbleList, Prompts, Sender, Welcome, XMarkdown, Thinking } from 'vue-element-plus-x'
import { ChromeFilled, Cpu, Promotion } from '@element-plus/icons-vue'
import type { PromptsItemsProps } from 'vue-element-plus-x/types/Prompts'

type MessageItem = BubbleListItemProps & {
  avatar: string
  role: 'ai' | 'user'
  thinkingStatus?: ThinkingStatus
  thinkCollapse?: boolean
  reasoningContent?: string
}

const { loading, run, onMessage, onError, cancel } = useChat()
// 表单
const chatForm = reactive({
  conversationId: '',
  prompt: ''
})

// 会话 id
const chatId = defineModel<string>()
// 刷新侧边栏
const emits = defineEmits(['refresh'])
const isNewConversation = ref(false)

// 会话列表
const bubbleListRef = ref<BubbleListInstance>()
const bubbleListItems = ref<MessageItem[]>([])
// 监听会话id
watch(chatId, (value) => {
  if (value) {
    // 如果会话 id 不为空，并且不是新创建的会话，从数据库中加载会话
    if (!isNewConversation.value) {
      chatForm.conversationId = value
      reqGetChatConversationList(value).then((res) => {
        const data = res.data
        convertBubbleListItem(data)
      })
    }
    // 如果会话id为空，清空会话
  } else {
    bubbleListItems.value = []
  }
})
// 消息转换
function convertBubbleListItem(messages: ChatMessageDto[]) {
  const result: MessageItem[] = []
  for (let i = 0; i < messages.length; i++) {
    const message = messages[i]

    const messageType = message?.messageType
    result.push(createMessage(messageType === 'USER', true, message?.text))
  }
  bubbleListItems.value = result
  nextTick(() => {
    if (bubbleListItems.value.length > 0) {
      bubbleListRef.value?.scrollToBottom()
    }
  })
}

const isSelect = ref(false)

// 提示词集
function promptItemClickHandler(item: PromptsItemsProps) {
  console.log('promptItemClickHandler', item)
}
const promptItems = ref<PromptsItemsProps[]>([
  {
    key: '1',
    label: '🐛 提示集组件标题',
    description: '描述信息'
  },
  {
    key: '2',
    label: '🐛 提示集组件标题',
    description: '描述信息'
  },
  {
    key: '3',
    label: '🐛 提示集组件标题',
    description: '描述信息'
  },
  {
    key: '4',
    label: '🐛 提示集组件标题',
    description: '描述信息'
  }
])

// 聊天提交
async function onSubmit() {
  // 清空输入框
  const prompt = chatForm.prompt
  chatForm.prompt = ''

  try {
    // 如果会话 id为空，创建会话 -> 刷新会话列表 -> 切换到新创建的会话
    if (!chatId.value) {
      isNewConversation.value = true
      const result = await reqAddChatConversation(prompt)
      emits('refresh')
      chatId.value = result.data
      chatForm.conversationId = result.data
    }

    // 创建消息
    bubbleListItems.value.push(createMessage(true, false, prompt))
    bubbleListItems.value.push(createMessage(false, false))
    // 聊天请求
    await run(chatForm.conversationId, prompt)
  } catch (e) {
    console.error('Fetch error:', e)
  } finally {
    // 完成清除状态
    const lastItem = bubbleListItems.value[bubbleListItems.value.length - 1]
    if (lastItem) {
      lastItem.loading = false
      lastItem.thinkingStatus = 'end'
    }
    isNewConversation.value = false
  }
}

onMessage((data) => {
  const lastItem = bubbleListItems.value[bubbleListItems.value.length - 1]!
  // 当前为加载状态 && 遇到第一个非空内容，取消加载状态
  if (lastItem.loading && data.content) {
    lastItem.loading = false
  }

  // 当前思考为折叠状态 && 第一个非空思考内容，开启思考状态，并且打开折叠
  if (!lastItem.thinkCollapse && data.reasoningContent) {
    lastItem.thinkingStatus = 'thinking'
    lastItem.thinkCollapse = true
  }

  lastItem.reasoningContent += data.reasoningContent
  lastItem.content += data.content
})

onError((message) => {
  const lastItem = bubbleListItems.value[bubbleListItems.value.length - 1]
  if (lastItem) {
    lastItem.thinkingStatus = 'error'
  }
  ElMessage.error(message)
})

/**
 * 创建消息
 * @param isUser 是否是用户消息
 * @param isHistory 是否是历史消息
 * @param message 消息
 */
function createMessage(isUser: boolean, isHistory: boolean, message = ''): MessageItem {
  return {
    placement: isUser ? 'end' : 'start',
    avatar: isUser
      ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
      : 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80',
    loading: !isUser && !isHistory,
    shape: 'corner',
    variant: isUser ? 'outlined' : 'filled',
    avatarSize: '36px',
    avatarGap: '12px',
    avatarShape: 'circle',
    content: message,
    isMarkdown: !isUser,
    typing: !isUser && !isHistory,
    isFog: !isUser,
    thinkingStatus: 'start',
    thinkCollapse: false,
    reasoningContent: '',
    role: isUser ? 'user' : 'ai'
  }
}
</script>

<template>
  <div class="container">
    <div class="bubble-list">
      <!-- 聊天会话列表 -->
      <BubbleList v-if="chatId" ref="bubbleListRef" :list="bubbleListItems" max-height="100%">
        <template #header="{ item }">
          <Thinking
            v-if="item.reasoningContent"
            v-model="item.thinkCollapse"
            :content="item.reasoningContent"
            :status="item.thinkingStatus"
            class="thinking-chain-warp"
          />
        </template>

        <template #content="{ item }">
          <!-- chat 内容走 markdown -->
          <XMarkdown
            v-if="item.role === 'ai'"
            :markdown="item.content!"
            :themes="{ light: 'github-light', dark: 'github-dark' }"
            class="markdown-body"
            default-theme-mode="dark"
          />
          <!-- user 内容 纯文本 -->
          <div v-if="item.role === 'user'" class="user-content">
            {{ item.content }}
          </div>
        </template>
      </BubbleList>

      <!-- 欢迎卡片 -->
      <Welcome
        v-else
        class="welcome"
        description="这是描述信息 ~"
        extra="副标题"
        icon="https://camo.githubusercontent.com/4ea7fdaabf101c16965c0bd3ead816c9d7726a59b06f0800eb7c9a30212d5a6a/68747470733a2f2f63646e2e656c656d656e742d706c75732d782e636f6d2f656c656d656e742d706c75732d782e706e67"
        title="欢迎使用 Element Plus X 💖"
      />
    </div>
    <!-- 提示词集 -->
    <Prompts
      v-if="!chatId"
      :items="promptItems"
      class="prompts"
      title="🐵 提示集组件标题"
      @itemClick="promptItemClickHandler"
    />

    <!-- 发送框 -->
    <Sender
      v-model="chatForm.prompt"
      :auto-size="{ minRows: 4, maxRows: 4 }"
      class="sender"
      placeholder="💌 在这里你可以自定义变体后的 prefix 和 action-list"
      variant="updown"
      @submit="onSubmit"
    >
      <template #prefix>
        <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap">
          <div :class="{ isSelect }" class="chat-option" @click="isSelect = !isSelect">
            <el-icon>
              <ChromeFilled />
            </el-icon>
            <span>联网搜索</span>
          </div>

          <div :class="{ isSelect }" class="chat-option" @click="isSelect = !isSelect">
            <el-icon>
              <Cpu />
            </el-icon>
            <span>深度思考</span>
          </div>
        </div>
      </template>
      <template #action-list>
        <div style="display: flex; align-items: center; gap: 8px">
          <el-button color="#626aef" round>
            <el-icon>
              <Promotion />
            </el-icon>
          </el-button>
        </div>
      </template>
    </Sender>
  </div>
</template>

<style lang="scss" scoped>
:deep(.el-bubble-start) {
  padding-left: 20px;
}

:deep(.el-bubble-end) {
  padding-right: 20px;
}

.container {
  display: flex;
  height: 100%;
  flex-direction: column;
  justify-content: space-between;

  .bubble-list {
    max-height: calc(100vh - $base_header_height - 40px - 160px - 40px);
    flex: 1;

    .welcome {
      margin: 0 20px;
    }

    .thinking-chain-warp {
      margin-bottom: 12px;
    }

    .markdown-body {
      background-color: transparent;
      font-size: var(--el-font-size-base);
    }

    .user-content {
      white-space: pre-wrap;
    }
  }

  .prompts {
    margin: 0 20px;
  }

  .sender {
    margin: 20px;

    .chat-option {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 2px 12px;
      border: 1px solid silver;
      border-radius: 15px;
      cursor: pointer;
      line-height: 24px;
    }

    .isSelect {
      color: #626aef;
      border: 1px solid #626aef !important;
      border-radius: 15px;
      padding: 3px 12px;
    }
  }
}
</style>
