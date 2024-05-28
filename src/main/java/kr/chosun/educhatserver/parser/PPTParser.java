package kr.chosun.educhatserver.parser;

import org.apache.poi.xslf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PPTParser implements Parser {
    public List<ParsedPage> parse(InputStream fileStream) {
        List<ParsedPage> parsedPage = new ArrayList<>();
        try {
            XMLSlideShow pptxshow = new XMLSlideShow(fileStream);

            List<XSLFSlide> slideList = pptxshow.getSlides();
            List<List<XSLFTextShape>> texts = new ArrayList<>();
            for(XSLFSlide slide : slideList) {
                List<XSLFTextShape> textShape = new ArrayList<>();
                slide.getShapes().forEach((shape) -> {
                    if(shape instanceof XSLFTextShape ts) {
                        textShape.add(ts);
                    } else if (shape instanceof XSLFGroupShape gs) {
                        textShape.addAll(getGroupShapeTexts(gs));
                    }
                });
                texts.add(textShape);
            }

            texts.forEach(slide -> {
                List<ParsedPageContent> contents = slide.stream().map((pageText) -> {
                    return new ParsedPageContent(pageText.getText());
                }).toList();
                ParsedPage page = new ParsedPage(contents);
                parsedPage.add(page);
            });

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
