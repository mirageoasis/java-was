package http;

import http.startline.RequestLine;
import http.startline.StartLine;
import java.util.Arrays;

public class Http {

    private final StartLine startLine;
    private final Header header;
    private final String body;

    private Http(StartLine startLine, Header header, String body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public static Http stringToRequest(String httpRequest) {
        String[] splitString = splitString(httpRequest);

        StartLine startLine = RequestLine.fromString(splitString[0]);
        Header header = Header.fromString(splitString[1]);
        String body = splitString[2];
        return new Http(startLine, header, body);
    }

    private static String[] splitString(String httpString) {
        if (httpString == null || httpString.isEmpty()) {
            throw new IllegalArgumentException("HTTP 메시지는 null이거나 비어 있을 수 없습니다.");
        }

        // 메시지를 start-line + 헤더와 바디로 분리
        String[] startLineAndHeaders = httpString.split("\r\n\r\n", 2);
        if (startLineAndHeaders.length < 1) {
            throw new IllegalArgumentException("잘못된 HTTP 메시지 형식: 헤더와 바디 분리가 없습니다.");
        }

        String startLineAndHeadersPart = startLineAndHeaders[0];
        String bodyPart = startLineAndHeaders.length > 1 ? startLineAndHeaders[1] : "";

        // start-line과 헤더를 줄 단위로 분리
        String[] lines = startLineAndHeadersPart.split("\r\n");
        if (lines.length < 1) {
            throw new IllegalArgumentException("잘못된 HTTP 메시지 형식: start-line과 헤더가 없습니다.");
        }

        // 첫 번째 줄은 start-line
        String startLine = lines[0];
        if (startLine.isEmpty()) {
            throw new IllegalArgumentException("잘못된 HTTP 메시지 형식: start-line이 없습니다.");
        }

        // 나머지는 헤더, 헤더가 없을 경우 빈 문자열로 처리
        String headers = "";
        if (lines.length > 1) {
            headers = String.join("\r\n", Arrays.copyOfRange(lines, 1, lines.length));
        }

        return new String[]{startLine, headers, bodyPart};
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public Header getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
