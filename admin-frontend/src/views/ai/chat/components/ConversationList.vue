<script lang="ts" setup>
import { type ChatMessageDto, reqGetChatConversationList } from '@/api/ai/chat'
import type { BubbleListInstance, BubbleListItemProps } from 'vue-element-plus-x/types/BubbleList'
import { BubbleList, Prompts, Sender, Welcome } from 'vue-element-plus-x'
import { ChromeFilled, Cpu, Promotion } from '@element-plus/icons-vue'
import type { PromptsItemsProps } from 'vue-element-plus-x/types/Prompts'

const porps = defineProps({
  chatId: {
    type: String,
    required: false
  }
})

// 会话列表
const bubbleListRef = ref<BubbleListInstance>()
const bubbleListItems = ref<BubbleListItemProps[]>([])
watch(
  () => porps.chatId,
  (value) => {
    if (value) {
      reqGetChatConversationList(value).then((res) => {
        const data = res.data
        convertBubbleListItem(data)
      })
    } else {
      bubbleListItems.value = []
    }
  }
)
function convertBubbleListItem(messages: ChatMessageDto[]) {
  const result: BubbleListItemProps[] = []
  for (let i = 0; i < messages.length; i++) {
    const message = messages[i]

    const messageType = message?.messageType
    result.push({
      placement: messageType === 'USER' ? 'end' : 'start',
      avatar:
        messageType === 'USER'
          ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          : 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif?imageView2/1/w/80/h/80',
      loading: false,
      shape: 'corner',
      variant: messageType === 'USER' ? 'outlined' : 'filled',
      avatarSize: '36px',
      avatarGap: '12px',
      avatarShape: 'circle',
      content: message?.text,
      isMarkdown: false,
      typing: false,
      isFog: messageType !== 'USER'
    })
  }
  bubbleListItems.value = result
  nextTick(() => {
    if (bubbleListItems.value.length > 0) {
      bubbleListRef.value?.scrollToBottom()
    }
  })
}

const senderValue = ref('')
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
</script>

<template>
  <div class="container">
    <div class="bubble-list">
      <!-- 聊天会话列表 -->
      <BubbleList v-if="chatId" ref="bubbleListRef" :list="bubbleListItems" max-height="100%" />

      <!-- 欢迎卡片 -->
      <Welcome
        v-else
        class="welcome"
        icon="https://camo.githubusercontent.com/4ea7fdaabf101c16965c0bd3ead816c9d7726a59b06f0800eb7c9a30212d5a6a/68747470733a2f2f63646e2e656c656d656e742d706c75732d782e636f6d2f656c656d656e742d706c75732d782e706e67"
        title="欢迎使用 Element Plus X 💖"
        extra="副标题"
        description="这是描述信息 ~"
      />
    </div>
    <!-- 提示词集 -->
    <Prompts
      v-if="!chatId"
      class="prompts"
      title="🐵 提示集组件标题"
      :items="promptItems"
      @itemClick="promptItemClickHandler"
    />

    <!-- 发送框 -->
    <Sender
      v-model="senderValue"
      :auto-size="{ minRows: 4, maxRows: 4 }"
      allow-speech
      class="sender"
      clearable
      placeholder="💌 在这里你可以自定义变体后的 prefix 和 action-list"
      variant="updown"
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
