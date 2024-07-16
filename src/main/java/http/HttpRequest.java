package http;

import static util.InputStreamUtil.readLineFromInputStream;

import http.startline.RequestLine;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.QueryParserUtil;

public class HttpRequest extends Http {

    private static final Logger logger = LoggerUtil.getLogger();

    public HttpRequest(RequestLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public static HttpRequest generateHttpRequest(InputStream inputStream)
        throws IOException {

        final String firstLine = readLineFromInputStream(inputStream);
        if (firstLine == null) {
            throw new IllegalArgumentException("HTTP 메시지가 비어 있습니다.");
        }

        RequestLine startLine = RequestLine.fromString(firstLine);
        Header header = Header.from(inputStream);
        byte[] body = generateBody(inputStream, header);
        logger.info("RequestLine: {}", startLine);
        logger.info("Header: {}", header);
        logger.info("Body: {}", new String(body));
        return new HttpRequest(startLine, header, body);
    }

    private static byte[] generateBody(InputStream inputStream, Header header)
        throws IOException {
        if (header.getValue("Content-Length").isEmpty()) {
            return new byte[0];
        }

        int contentLength = Integer.parseInt(header.getValue("Content-Length"));
        byte[] body = new byte[contentLength];
        inputStream.read(body, 0, contentLength);

        return body;
    }

    public Map<String, String> getBodyParams() {
        return QueryParserUtil.parseQuery(new String(this.getBody()));
    }
}
