import request from '@/utils/request'
import useChatSSE from '@/hooks/useChatSSE'

export interface ChatHistoryVo {
  conversationId: string
  title: string
}

// 获取聊天历史
export function reqGetChatHistory() {
  return request.get<ChatHistoryVo[]>('/ai/chat/history')
}

export interface ChatConversationRenameForm {
  conversationId: string
  title: string
}

// 重命名聊天会话
export function reqRenameChatConversationTitle(form: ChatConversationRenameForm) {
  return request.patch<void, ChatConversationRenameForm>('/ai/chat/conversation', form)
}

export interface ChatMessageDto {
  messageType: 'USER' | 'ASSISTANT'
  text: string
}

// 获取聊天会话
export function reqGetChatConversationList(conversationId: string) {
  return request.get<ChatMessageDto[]>(`/ai/chat/history/${conversationId}`)
}

// 新增聊天会话
export function reqAddChatConversation(title: string) {
  return request.post<string>('/ai/chat/conversation', title, {
    headers: {
      'Content-Type': 'text/plain'
    }
  })
}

// 删除聊天会话
export function reqDeleteChatConversationById(conversationId: string) {
  return request.delete<void>(`/ai/chat/conversation/${conversationId}`)
}

export interface ChatForm {
    conversationId: string
    prompt: string
    enableThinking: boolean
    enableSearch: boolean
}

// 聊天请求
export function useChat() {
  const { loading, run, onMessage, onError, cancel } = useChatSSE()

    function innerRun(chatForm: ChatForm) {
    return run('/ai/chat', {
      method: 'post',
        body: JSON.stringify(chatForm),
      headers: {
          'Content-Type': 'application/json'
      }
    })
  }

  return {
    loading,
    run: innerRun,
    onMessage,
    onError,
    cancel
  }
}
