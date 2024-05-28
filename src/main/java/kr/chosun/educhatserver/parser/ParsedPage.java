package kr.chosun.educhatserver.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@RequiredArgsConstructor
public class ParsedPage {
    private final List<ParsedPageContent> contents;
}
