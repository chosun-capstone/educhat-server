package kr.chosun.educhatserver.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TXTParser implements Parser {
    @Override
    public List<ParsedPage> parse(InputStream fileStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));


            List<ParsedPageContent> contents = new ArrayList<>();
            String str;
            while ((str = reader.readLine()) != null) {
                contents.add(new ParsedPageContent(str));
            }
            reader.close();
            ParsedPage page = new ParsedPage(contents);
            return List.of(page);
        }catch(IOException ioe) {
            throw new RuntimeException();
        }
    }
}
