package kr.chosun.educhatserver.openai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.chosun.educhatserver.openai.dto.*;
import kr.chosun.educhatserver.openai.entity.*;
import kr.chosun.educhatserver.openai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final FileProcessedRepository fileProcessedRepository;
    private final FileQuestionRepository fileQuestionRepository;
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
                "system", "너는 내가 보내는 내용을 페이지별로 요약해서 한국어로 보내줘. 필요없는 특수문자 빼버리고 깔끔하게 요약해줘. 아무 내용도 없더라도 적어도 간단한 설명 넣어서 모든 페이지 있게 보내줘. 나열하지말고 쭉 서술형으로 설명해줘. 최대한 구체적으로 설명해줘야해. 사용자에게 보여줄거라 개행이나 숫자같은거 넣어서 설명해도돼. content는 html 써서 보기좋게 줄바꿈이랑 list 같은거 넣어서 보내줘. 형식은 다음과 같은 JSON으로 보내줘. 길이수 제한 안걸리게 생성해줘. [{\"page\": 페이지번호,\"content\":\"너가요약한내용\"}...]"
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

            List<FileProcessedEntity> entities = new ArrayList<>();
            for (GPTPromptDTO dto : dtos) {
                System.out.println("PAGE : " + dto.getPage() +", Content:" + dto.getContent());
                entities.add(new FileProcessedEntity(fileId, (long) dto.getPage(), dto.getContent()));
            }
            fileEntity.setStatus(FileStatus.PROCESSED);
            fileRepository.save(fileEntity);
            fileProcessedRepository.saveAllAndFlush(entities);
        }catch(Exception ex) {
            ex.printStackTrace();
            fileEntity.setStatus(FileStatus.FAILED);
            fileRepository.save(fileEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void beginGPTQues(long fileId){
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
                "system", "너는 내가 보낸 본문에 대한 문제를 여러개 내줘. 5지선다 문제 혹은 서답형, 서술형 등 문제 내줘. 너가 생각하기에 내용 이해해 필요하다 싶은 개수만큼 내주고, 다음 형식처럼 보내주고, 정답에 대한 설명도 포함해줘. question 속성은 html을 써서 줄바꿈이나 꾸며도 돼. [{\"question\": \"질문내용\", \"answer\": \"정답내용\", \"explain\": \"설명\"}...]"
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
        });
        prompts.add(new Message("user", sb.toString()));
        ChatGPTRequest request = new ChatGPTRequest(model, prompts);
        ChatGPTResponse response = getChatGPTResponse(request);
        ChatGPTResponse.Choice result = response.getChoices().get(0);
        try {
            System.out.println(result.getMessage().getContent());
            GPTPromptQues[] dtos = objectMapper.readValue(result.getMessage().getContent(), GPTPromptQues[].class);

            List<FileQuestionEntity> entities = new ArrayList<>();
            for (int i = 0; i < dtos.length; i++) {
                GPTPromptQues dto = dtos[i];
                System.out.println(dto);
                entities.add(new FileQuestionEntity(fileId, (long) i, dto.getQuestion(), dto.getAnswer(), dto.getExplain()));
            }
            fileQuestionRepository.saveAllAndFlush(entities);
        }catch(Exception ex) {
            ex.printStackTrace();
            fileEntity.setStatus(FileStatus.FAILED);
            fileRepository.save(fileEntity);
        }
    }
}
