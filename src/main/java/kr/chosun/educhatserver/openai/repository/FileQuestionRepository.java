package kr.chosun.educhatserver.openai.repository;

import kr.chosun.educhatserver.openai.entity.FileQuestionEntity;
import kr.chosun.educhatserver.openai.entity.FileQuestionEntityKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileQuestionRepository extends JpaRepository<FileQuestionEntity, FileQuestionEntityKey> {
    List<FileQuestionEntity> findAllByFileId(Long fileId);
}
