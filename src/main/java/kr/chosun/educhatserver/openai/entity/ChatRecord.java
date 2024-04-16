package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatRecord")
public class ChatRecord {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq")
    private Long id;

    @NotEmpty
    @Column(name = "userId")
    private String userId;

    @Column(name = "userMessage")
    private String userMessage;

    @Column(name = "promptMessage")
    private String promptMessage;

    @CreatedDate
    @Column(name = "localDateTime")
    private LocalDateTime localDateTime;

}
