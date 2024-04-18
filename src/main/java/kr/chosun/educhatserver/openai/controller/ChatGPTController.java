package kr.chosun.educhatserver.openai.controller;

import jakarta.validation.Valid;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public ResponseEntity chat(@RequestBody @Valid String prompt) {
        ChatGPTRequest request = chatGPTService.setChatGPTRequest(prompt);
        ChatGPTResponse response = chatGPTService.getChatGPTResponse(request);
        String content = null;

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            content = response.getChoices().get(0).getMessage().getContent();
        }

        if (content == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(content);
        }

        chatGPTService.saveChatRecord(request, response);
        return ResponseEntity.ok(content);
    }
}
