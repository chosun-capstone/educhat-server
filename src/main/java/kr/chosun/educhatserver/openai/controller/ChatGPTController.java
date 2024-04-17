package kr.chosun.educhatserver.openai.controller;

import java.util.Map;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.service.ChatGPTService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    private ChatGPTService chatGPTService;

    @GetMapping("/chat")
    public String chat(@RequestBody String prompt) {
        String response = chatGPTService.getChatGPTResponse(prompt);
        return response != null ? response : "응답을 생성할 수 없습니다.";
    }
}
