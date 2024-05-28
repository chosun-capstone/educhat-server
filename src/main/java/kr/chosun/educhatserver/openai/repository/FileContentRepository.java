package kr.chosun.educhatserver.openai.repository;

import kr.chosun.educhatserver.openai.entity.FileContentEntity;
import kr.chosun.educhatserver.openai.entity.FileContentEntityKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileContentRepository extends JpaRepository<FileContentEntity, FileContentEntityKey> {
    List<FileContentEntity> findAllByFileId(Long fileId);
}
