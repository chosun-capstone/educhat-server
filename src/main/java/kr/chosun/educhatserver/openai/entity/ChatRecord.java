package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ChatRecord")
public class ChatRecord {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * login username
     */
//    @NotEmpty
//    @Column(name = "userId")
//    private String userId;

    @Column(name = "userMessage")
    private String userMessage;

    @Column(name = "botMessage")
    private String botMessage;

    @CreationTimestamp
    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

}
