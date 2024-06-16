package kr.chosun.educhatserver.openai.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GPTPromptQues {
    private final String question;
    private final String answer;
    private final String explain;
}
