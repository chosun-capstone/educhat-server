package kr.chosun.educhatserver.openai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileContentEntityKey implements Serializable {
    private Long fileId;
    private Long page;
    private Long itemIndex;
}
