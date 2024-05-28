package kr.chosun.educhatserver.openai.service;

import jakarta.transaction.Transactional;
import kr.chosun.educhatserver.openai.entity.FileContentEntity;
import kr.chosun.educhatserver.openai.entity.FileEntity;
import kr.chosun.educhatserver.openai.entity.FileStatus;
import kr.chosun.educhatserver.openai.repository.FileContentRepository;
import kr.chosun.educhatserver.openai.repository.FileRepository;
import kr.chosun.educhatserver.parser.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class FileAsyncService {
    private final FileRepository fileRepository;
    private final FileContentRepository fileContentRepository;
    private final ChatGPTService gptService;

    @Async
    public void beginParse(FileEntity fileEntity) {
        String extension = FilenameUtils.getExtension(fileEntity.getFileName());
        Parser parser = switch(extension) {
            case "pptx":
            case "ppt":
                yield new PPTParser();
            case "docx":
            case "doc":
                yield new WordParser();
            case "txt":
                yield new TXTParser();
            case "pdf":
                yield new PDFParserImpl();
            default:
                throw new RuntimeException(); //앞에서 걸렀는데 일어날리가..
        };

        List<ParsedPage> pages = parser.parse(new ByteArrayInputStream(fileEntity.getData()));
        List<FileContentEntity> fce = new ArrayList<>();
        for (int i = 0; i < pages.size(); i++) {
            ParsedPage page = pages.get(i);
            for (int i1 = 0; i1 < page.getContents().size(); i1++) {
                ParsedPageContent content = page.getContents().get(i1);
                fce.add(
                        new FileContentEntity(fileEntity.getId(), (long) i, (long) i1, content.getText())
                );
            }
        }
        fileEntity.setStatus(FileStatus.PARSED);
        fileRepository.save(fileEntity);
        fileContentRepository.saveAllAndFlush(fce);
        gptService.beginGPT(fileEntity.getId());
    }
}
