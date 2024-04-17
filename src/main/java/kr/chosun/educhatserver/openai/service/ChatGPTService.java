package kr.chosun.educhatserver.openai.service;

import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.entity.ChatRecord;
import kr.chosun.educhatserver.openai.repository.ChatRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private final ChatRecordRepository repository;

    public ChatGPTResponse getChatGPTResponse(ChatGPTRequest request) {
        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
        return response;
    }

    public ChatGPTRequest setChatGPTRequest(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        return request;
    }

    public void saveChatRecord(ChatGPTRequest request, ChatGPTResponse response) {
        ChatRecord chatRecord = ChatRecord.builder()
                .userMessage(request.getMessages().get(0).getContent())
                .botMessage(response.getChoices().get(0).getMessage().getContent())
                .build();

        repository.save(chatRecord);
    }
}
