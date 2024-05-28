package kr.chosun.educhatserver.openai.controller;

import kr.chosun.educhatserver.openai.entity.FileEntity;
import kr.chosun.educhatserver.openai.entity.FileStatus;
import kr.chosun.educhatserver.openai.repository.FileRepository;
import kr.chosun.educhatserver.openai.service.FileAsyncService;
import kr.chosun.educhatserver.parser.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController//("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileAsyncService fileAsyncService;

    @PostMapping("/file")
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(extension == null || !isSupportedExtension(extension)) throw new RuntimeException();

        FileEntity fileEntity = null;
        try {
            fileEntity = new FileEntity(-1L, file.getOriginalFilename(), FileStatus.UPLOADED, file.getBytes());
        }catch(IOException ioe){

        }
        fileEntity = fileRepository.save(fileEntity);
        fileAsyncService.beginParse(fileEntity);
    }

    public boolean isSupportedExtension(String extension) {
        return switch(extension) {
            case "pptx", "ppt", "docx", "doc", "txt", "pdf" -> true;
            default -> false;
        };
    }
}
