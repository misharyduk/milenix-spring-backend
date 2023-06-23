package com.project.milenix.article_service.chat_gpt.controller;

import com.project.milenix.article_service.chat_gpt.dto.CharContentRequestDto;
import com.project.milenix.article_service.chat_gpt.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PreAuthorize("hasAuthority('article:write')")
    @MessageMapping("/get-chat-categories")
    @SendTo("/topic/chat-topic")
    public List<String> categories(CharContentRequestDto dto) throws Exception{
        return chatGPTService.chatWithGpt3(dto.content());
    }
}
