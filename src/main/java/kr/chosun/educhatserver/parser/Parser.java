package kr.chosun.educhatserver.parser;

import java.io.InputStream;
import java.util.List;

public interface Parser {
    List<ParsedPage> parse(InputStream fileStream);
}
