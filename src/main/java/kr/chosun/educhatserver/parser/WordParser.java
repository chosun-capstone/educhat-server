package kr.chosun.educhatserver.parser;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WordParser implements Parser {
    public List<ParsedPage> parse(InputStream fileStream) {
        List<ParsedPage> parsedPage = new ArrayList<>();
        try {
            XWPFDocument document = new XWPFDocument(fileStream);

            List<String> pages = new ArrayList<>();
            StringBuilder currentPage = new StringBuilder();
            int pageHeight = getPageHeight(document);

            int currentHeight = 0;

            for (IBodyElement element : document.getBodyElements()) {
                if (element instanceof XWPFParagraph) {
                    XWPFParagraph para = (XWPFParagraph) element;
                    int paraHeight = estimateParagraphHeight(para);
                    if (currentHeight + paraHeight > pageHeight) {
                        pages.add(currentPage.toString());
                        currentPage = new StringBuilder();
                        currentHeight = 0;
                    }
                    currentPage.append(para.getText()).append("\n");
                    currentHeight += paraHeight;
                } else if (element instanceof XWPFTable) {
                    XWPFTable table = (XWPFTable) element;
                    int tableHeight = estimateTableHeight(table);
                    if (currentHeight + tableHeight > pageHeight) {
                        pages.add(currentPage.toString());
                        currentPage = new StringBuilder();
                        currentHeight = 0;
                    }
                    currentPage.append(table.getText()).append("\n");
                    currentHeight += tableHeight;
                }
            }

            // 마지막 페이지 추가
            if (!currentPage.isEmpty()) {
                pages.add(currentPage.toString());
            }

            // 페이지 출력
            pages.forEach((pageStr) -> {
                List<ParsedPageContent> contents = new ArrayList<>();
                for (String line : pageStr.split("\n")) {
                    contents.add(new ParsedPageContent(line));
                }
                ParsedPage page = new ParsedPage(contents);
                parsedPage.add(page);
            });

            document.close();

            return parsedPage;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static int getPageHeight(XWPFDocument document) {
        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        if (sectPr == null) {
            sectPr = document.getDocument().getBody().addNewSectPr();
        }
        if (sectPr.isSetPgSz()) {

            return ((BigInteger) sectPr.getPgSz().getH()).intValue();
        }
        // 기본 페이지 높이 (A4 사이즈)
        return 842 * 20; // TWIP 단위 (1/20 포인트)
    }

    private static int estimateParagraphHeight(XWPFParagraph para) {
        int lineHeight = 240; // 기본 라인 높이 (TWIP 단위)
        int numLines = para.getText().split("\n").length;
        return lineHeight * numLines;
    }

    private static int estimateTableHeight(XWPFTable table) {
        int rowHeight = 240; // 기본 행 높이 (TWIP 단위)
        int numRows = table.getRows().size();
        return rowHeight * numRows;
    }
}
