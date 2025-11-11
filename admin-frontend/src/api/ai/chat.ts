import request from '@/utils/request'

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

// 删除聊天会话
export function reqDeleteChatConversationById(conversationId: string) {
  return request.delete<void>(`/ai/chat/conversation/${conversationId}`)
}
