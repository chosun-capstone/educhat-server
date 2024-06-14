package kr.chosun.educhatserver.openai.repository;

import kr.chosun.educhatserver.openai.entity.FileEntity;
import kr.chosun.educhatserver.openai.entity.FileProcessedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileProcessedRepository extends JpaRepository<FileProcessedEntity, Long> {
    List<FileProcessedEntity> findAllByFileId(Long fileId);
}
