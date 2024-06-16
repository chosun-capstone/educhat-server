package kr.chosun.educhatserver.openai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileQuestionEntityKey implements Serializable {
    private Long fileId;
    private Long idx;
}
