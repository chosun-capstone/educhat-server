package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import kr.chosun.educhatserver.authentication.member.domain.Member;
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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member user;

    @Column(name = "userMessage")
    private String userMessage;

    @Lob
    @Column(name = "botMessage", columnDefinition = "TEXT")
    private String botMessage;

    @CreationTimestamp
    @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

}
