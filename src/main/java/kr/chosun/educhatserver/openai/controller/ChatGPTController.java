package kr.chosun.educhatserver.openai.controller;

import jakarta.validation.Valid;
import java.util.Map;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.service.ChatGPTService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGPTController {
//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt")String prompt) {
//        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
//
//        return response.getChoices().get(0).getMessage().getContent();
//    }

    private final ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public ResponseEntity chat(@RequestBody @Valid String prompt) {
        ChatGPTRequest request = chatGPTService.setChatGPTRequest(prompt);
        ChatGPTResponse response = chatGPTService.getChatGPTResponse(request);
        String content = null;

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            content = response.getChoices().get(0).getMessage().getContent();
        }

        if(content == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(content);
        }

        chatGPTService.saveChatRecord(request, response);
        return ResponseEntity.ok(content);
    }
}
