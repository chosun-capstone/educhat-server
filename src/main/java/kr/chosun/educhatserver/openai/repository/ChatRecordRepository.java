package kr.chosun.educhatserver.openai.repository;

import kr.chosun.educhatserver.openai.entity.ChatRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRecordRepository extends JpaRepository<ChatRecord, Long> {g
}
