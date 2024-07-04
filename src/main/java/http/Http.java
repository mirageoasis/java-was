package http;

import http.startline.StartLine;

public class Http {

    private StartLine startLine;
    private final Header header;
    private byte[] body;

    protected Http(StartLine startLine, Header header, byte[] body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
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
