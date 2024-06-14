package kr.chosun.educhatserver.openai.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import kr.chosun.educhatserver.openai.dto.FileDTO;
import kr.chosun.educhatserver.openai.entity.FileDataEntity;
import kr.chosun.educhatserver.openai.entity.FileEntity;
import kr.chosun.educhatserver.openai.entity.FileProcessedEntity;
import kr.chosun.educhatserver.openai.entity.FileStatus;
import kr.chosun.educhatserver.openai.repository.FileProcessedRepository;
import kr.chosun.educhatserver.openai.repository.FileRepository;
import kr.chosun.educhatserver.openai.service.FileAsyncService;
import kr.chosun.educhatserver.parser.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController//("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileProcessedRepository fileProcessedRepository;
    private final FileAsyncService fileAsyncService;

    @PostMapping("/files")
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(extension == null || !isSupportedExtension(extension)) throw new RuntimeException();

        FileEntity fileEntity = null;
        try {
            fileEntity = new FileEntity(-1L, file.getOriginalFilename(), FileStatus.UPLOADED,
                    null);
            fileEntity.setFileData(new FileDataEntity(-1L, file.getBytes(), fileEntity));
        }catch(IOException ioe){

        }
        fileEntity = fileRepository.save(fileEntity);
        fileAsyncService.beginParse(fileEntity);
    }

    @GetMapping("/files")
    public List<FileDTO> getFiles() {
        return fileRepository.findAll().stream().map((e) -> new FileDTO(e.getId(), e.getFileName())).toList();
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<InputStreamSource> downloadFile(@PathVariable Long fileId, HttpServletRequest req) {
        FileEntity fileEntity = fileRepository.findById(fileId).orElseThrow(RuntimeException::new);
        byte[] arr = fileEntity.getFileData().getData();
        MediaType mediaType = getMediaTypeForFileName(req.getServletContext(), fileEntity.getFileName());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileEntity.getFileName()) // file.getName()은 원본 파일명
                .contentType(mediaType)
                .contentLength(arr.length)
                .body(new InputStreamResource(new ByteArrayInputStream(arr)));
    }

    @GetMapping("/summary/{fileId}")
    public List<FileProcessedEntity> getSummary(@PathVariable Long fileId) {
        return fileProcessedRepository.findAllByFileId(fileId);
    }

    public boolean isSupportedExtension(String extension) {
        return switch(extension) {
            case "pptx", "ppt", "docx", "doc", "txt", "pdf" -> true;
            default -> false;
        };
    }

    private MediaType getMediaTypeForFileName(ServletContext servletContext, String filename) {

        String minType = servletContext.getMimeType(filename);

        try {
            MediaType mediaType = MediaType.parseMediaType(minType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }


    }
}
