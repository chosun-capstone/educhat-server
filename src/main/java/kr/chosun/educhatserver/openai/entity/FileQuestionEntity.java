package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FileQuestionEntity")
@IdClass(FileQuestionEntityKey.class)
public class FileQuestionEntity {
    @Id
    @Column(name = "file_id")
    private Long fileId;

    @Id
    @Column(name = "idx")
    private Long idx;

    @Column(name = "ques", length = 10000, columnDefinition = "TEXT")
    private String question;

    @Column(name = "ans", length = 10000, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "descr", length = 10000, columnDefinition = "TEXT")
    private String explain;
}


