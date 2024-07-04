package http;

import http.startline.ResponseLine;
import java.io.IOException;

public class HttpResponse extends Http{
    private final byte[] body;

    public HttpResponse(ResponseLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
        this.body = body;
    }

    public static HttpResponse generateHttpResponse() throws IOException {
        return new HttpResponse(new ResponseLine(), Header.emptyHeader(), null);
    }

    public byte[] getBody() {
        return body;
    }
}
