package com.yeeiee.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeeiee.ai.domain.entity.ChatHistory;
import com.yeeiee.ai.mapper.ChatHistoryMapper;
import com.yeeiee.ai.service.ChatHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * AI对话记录表 服务实现类
 * </p>
 *
 * @author chen
 * @since 2025-11-11
 */
@Service
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

}