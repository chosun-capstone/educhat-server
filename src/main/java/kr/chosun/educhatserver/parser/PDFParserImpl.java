package kr.chosun.educhatserver.parser;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PDFParserImpl implements Parser {
    public List<ParsedPage> parse(InputStream fileStream) {
        List<ParsedPage> parsedPage = new ArrayList<>();
        try {
            PDDocument pdf = Loader.loadPDF(fileStream.readAllBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            for(int i = 1; i <= pdf.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String text = stripper.getText(pdf);
                List<ParsedPageContent> contents = new ArrayList<>();
                for(String line : text.split("\n")) {
                    contents.add(new ParsedPageContent(line));
                }
                ParsedPage page = new ParsedPage(contents);
                parsedPage.add(page);
            }

            return parsedPage;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public List<XSLFTextShape> getGroupShapeTexts(XSLFGroupShape groupShape) {
        List<XSLFTextShape> textShape = new ArrayList<>();
        for (XSLFShape shape : groupShape.getShapes()) {
            if(shape instanceof XSLFTextShape ts) {
                textShape.add(ts);
            } else if (shape instanceof XSLFGroupShape gs) {
                textShape.addAll(getGroupShapeTexts(gs));
            }
        }
        return textShape;
    }
}
