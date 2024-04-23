package kr.chosun.educhatserver.openai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;
    private final ObjectMapper objectMapper;

    /**
     * @param prompt (얿로드 파일 이름)
     * @return
     */
    @PostMapping("/chat")
    public ResponseEntity chat(@RequestBody @Valid String prompt) throws JsonProcessingException {

        /**
         * file 뒤에 명령 커스텀 ("요약해줘", "문제 만들어줘" 같은 커맨드를 생성해서 적용 시킬 예정)
         */

        String input = objectMapper.readValue(prompt, Map.class).get("prompt").toString() + " 요약해줘";
        log.info("input: " + input);

        ChatGPTRequest request = chatGPTService.setChatGPTRequest(input);
        ChatGPTResponse response = chatGPTService.getChatGPTResponse(request);
        String content = null;

        if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
            content = response.getChoices().get(0).getMessage().getContent();
        }

        if (content == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(content);
        }

        chatGPTService.saveChatRecord(input, response);

        return ResponseEntity.ok(content);
    }
}
