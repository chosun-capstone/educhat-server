package kr.chosun.educhatserver.openai.repository;

import kr.chosun.educhatserver.openai.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
