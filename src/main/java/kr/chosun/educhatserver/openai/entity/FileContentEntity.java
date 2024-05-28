package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FileContentEntity")
@IdClass(FileContentEntityKey.class)
public class FileContentEntity {
    @Id
    @Column(name = "file_id")
    private Long fileId;

    @Id
    @Column(name = "page")
    private Long page;

    @Id
    @Column(name = "item_index")
    private Long itemIndex;

    @Column(name = "content", length = 1000000)
    private String content;
}


