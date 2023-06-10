package com.project.milenix.article_service.chat_gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.milenix.article_service.chat_gpt.http_entities.CompletionRequest;
import com.project.milenix.article_service.chat_gpt.http_entities.CompletionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatGPTService {
    @Autowired
    private ObjectMapper jsonMapper;
    @Value("${openai.api_key}")
    private String openaiApiKey;
    private static final URI CHATGPT_URI = URI.create("https://api.openai.com/v1/completions");
    private HttpClient client = HttpClient.newHttpClient();

    public List<String> chatWithGpt3(String message) throws Exception{
        var request = HttpRequest.newBuilder()
                .uri(CHATGPT_URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                .POST(chatMessageAsPostBody(message))
                .build();
        var responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
        return Arrays.stream(completionResponse.firstAnswer()).limit(5).collect(Collectors.toList());
    }


    private HttpRequest.BodyPublisher chatMessageAsPostBody(String message) throws JsonProcessingException {
        var completion = CompletionRequest.defaultWith("Обери не більше п'яти загальних категорій через кому одним словом без крапки в кінці, до яких відноситься стаття: " + message + ". Дай повну відповідь.");
        return HttpRequest.BodyPublishers.ofString(jsonMapper.writeValueAsString(completion));
    }
}
