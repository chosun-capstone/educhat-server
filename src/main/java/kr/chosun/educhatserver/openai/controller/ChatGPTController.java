package kr.chosun.educhatserver.openai.controller;

import java.util.Map;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGPTController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;

//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt")String prompt) {
//        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
//        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
//
//        return response.getChoices().get(0).getMessage().getContent();
//    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);

        String content = response.getChoices().get(0).getMessage().getContent();

        return ResponseEntity.ok(content);
    }
}
