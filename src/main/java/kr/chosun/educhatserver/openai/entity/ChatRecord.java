package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import kr.chosun.educhatserver.security.entity.User;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "categories")
    private User user;

    @Column(name = "userMessage")
    private String userMessage;

    @Lob
    @Column(name = "botMessage", columnDefinition = "TEXT")
    private String botMessage;

    @CreationTimestamp
    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

}
