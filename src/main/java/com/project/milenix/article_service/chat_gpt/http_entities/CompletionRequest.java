package com.project.milenix.article_service.chat_gpt.http_entities;

public record CompletionRequest(String model, String prompt, double temperature, Integer max_tokens) {
    public static CompletionRequest defaultWith(String prompt){
        return new CompletionRequest("text-davinci-003", prompt, 0.7, 100);
    }
}
