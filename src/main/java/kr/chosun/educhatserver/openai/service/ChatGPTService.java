package kr.chosun.educhatserver.openai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.chosun.educhatserver.openai.dto.ChatGPTRequest;
import kr.chosun.educhatserver.openai.dto.ChatGPTResponse;
import kr.chosun.educhatserver.openai.dto.GPTPromptDTO;
import kr.chosun.educhatserver.openai.dto.Message;
import kr.chosun.educhatserver.openai.entity.ChatRecord;
import kr.chosun.educhatserver.openai.entity.FileContentEntity;
import kr.chosun.educhatserver.openai.entity.FileEntity;
import kr.chosun.educhatserver.openai.repository.ChatRecordRepository;
import kr.chosun.educhatserver.openai.repository.FileContentRepository;
import kr.chosun.educhatserver.openai.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatGPTService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private final ChatRecordRepository repository;
    private final FileRepository fileRepository;
    private final FileContentRepository fileContentRepository;
    private final ObjectMapper objectMapper;

    public ChatGPTResponse getChatGPTResponse(ChatGPTRequest request) {
        ChatGPTResponse response = template.postForObject(apiURL, request, ChatGPTResponse.class);
        return response;
    }

    @Transactional
    public ChatGPTRequest setChatGPTRequest(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        return request;
    }

    @Transactional
    public void saveChatRecord(String prompt, ChatGPTResponse response) {
        ChatRecord chatRecord = ChatRecord.builder()
                .userMessage(prompt)
                .botMessage(response.getChoices().get(0).getMessage().getContent())
                .build();

        repository.save(chatRecord);
    }

    @Async
    public void beginGPT(long fileId){
        FileEntity fileEntity = fileRepository.getReferenceById(fileId);
        List<FileContentEntity> fileContents = fileContentRepository.findAllByFileId(fileId);

        HashMap<Long, List<FileContentEntity>> groupMap = new HashMap<>();
        fileContents.forEach((content) -> {
            if(groupMap.containsKey(content.getPage())){
                groupMap.get(content.getPage()).add(content);
            }else{
                groupMap.put(content.getPage(), new ArrayList<>(List.of(content)));
            }
        });
        List<Message> prompts = new ArrayList<>();
        prompts.add(new Message(
                "system", "너는 내가 보내는 내용을 페이지별로 요약해서 한국어로 보내줘. 내용 안에 이상한 특수문자같은거 빼주고, 나열하지말고 쭉 서술형으로 설명해줘. 최대한 구체적으로 설명해줘야해. 사용자에게 보여줄거라 개행이나 숫자같은거 넣어서 설명해도돼. 형식은 다음과 같은 JSON으로 보내줘. [{\"page\": 페이지번호,\"content\":\"너가요약한내용\"}...]"
        ));
        StringBuilder sb = new StringBuilder();
        AtomicInteger pageIndex = new AtomicInteger(0);
        groupMap.forEach((page, contents) -> {
            sb.append("페이지 번호: " + pageIndex.incrementAndGet() + "\n");
            StringBuilder prompt = new StringBuilder();
            contents.stream()
                    .sorted(Comparator.comparingLong(FileContentEntity::getItemIndex))
                    .forEach((content) -> {
                        prompt.append(content.getContent());
                    });

            sb.append(prompt.toString() + "\n");
            //saveChatRecord(prompt.toString(), response);
        });
        prompts.add(new Message("user", sb.toString()));
        ChatGPTRequest request = new ChatGPTRequest(model, prompts);
        ChatGPTResponse response = getChatGPTResponse(request);
        ChatGPTResponse.Choice result = response.getChoices().get(0);
        try {
            GPTPromptDTO[] dtos = objectMapper.readValue(result.getMessage().getContent(), GPTPromptDTO[].class);
            for (GPTPromptDTO dto : dtos) {
                System.out.println("PAGE : " + dto.getPage() +", Content:" + dto.getContent());
            }
        }catch(Exception ex) {

        }

        System.out.println(response);
    }
}
