package kr.chosun.educhatserver.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GPTPromptDTO {
    private final int page;
    private final String content;
}
