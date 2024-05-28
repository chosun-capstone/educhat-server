package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FileProcessedEntity")
@IdClass(FileProcessedEntityKey.class)
public class FileProcessedEntity {
    @Id
    @Column(name = "file_id")
    private Long fileId;

    @Id
    @Column(name = "page")
    private Long page;

    @Column(name = "content", length = 1000000)
    private String content;
}


