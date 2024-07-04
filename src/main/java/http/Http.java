package http;

import http.startline.RequestLine;
import http.startline.ResponseLine;
import http.startline.StartLine;
import java.io.BufferedReader;
import java.io.IOException;

public class Http {

    private StartLine startLine;
    private final Header header;
    private byte[] body;

    private Http(StartLine startLine, Header header, String body) {
        this.startLine = startLine;
        this.header = header;
        this.body = null;
    }

    public static Http generateHttpRequest(BufferedReader bufferedReader) throws IOException {
        final String firstLine = bufferedReader.readLine();
        if (firstLine == null) {
            throw new IllegalArgumentException("HTTP 메시지가 비어 있습니다.");
        }

        StartLine startLine = RequestLine.fromString(firstLine);
        Header header = Header.from(bufferedReader);
        String body = generateBody(bufferedReader, header);
        return new Http(startLine, header, body);
    }

    public static Http generateHttpResponse() throws IOException {
        Header header = Header.emptyHeader();
        return new Http(new ResponseLine(), header, null);
    }

    private static String generateBody(BufferedReader bufferedReader, Header header) throws IOException {
        if (header.getHeader("Content-Length").isEmpty()) {
            return "";
        }

        int contentLength = Integer.parseInt(header.getHeader("Content-Length"));
        char[] body = new char[contentLength];
        bufferedReader.read(body, 0, contentLength);

        return new String(body);
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public Header getHeader() {
        return header;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setStartLine(StartLine startLine) {
        this.startLine = startLine;
    }

    public byte[] getBody() {
        return body;
    }
}
