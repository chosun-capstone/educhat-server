package kr.chosun.educhatserver.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileDTO {
    private final long fileId;
    private final String fileName;

}
