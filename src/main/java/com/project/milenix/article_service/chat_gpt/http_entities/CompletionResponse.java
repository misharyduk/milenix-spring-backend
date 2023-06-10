package com.project.milenix.article_service.chat_gpt.http_entities;

import java.util.List;

public record CompletionResponse(Usage usage, List<Choice> choices) {

    public String[] firstAnswer(){
        if(choices == null || choices.isEmpty())
            return null;
        String text = choices.get(0).text;
        String substring = text.substring(2);
        return substring.split("(\\d.\\s)|(, )");
    }

    record Usage(int total_tokens, int prompt_tokens, int completion_tokens){ }

    record Choice(String text) { }

}
