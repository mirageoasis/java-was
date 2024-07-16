package http;

import http.startline.RequestLine;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.QueryParserUtil;

public class HttpMultiPartRequest extends HttpRequest{

    private static final Logger logger = LoggerUtil.getLogger();
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT = "content";

    // bodyparam의 content-type을 저장하는 Map
    private Map<String, String> contentTypeParams = new HashMap<>();

    public HttpMultiPartRequest(RequestLine startLine, Header header, byte[] body) {
        super(startLine, header, body);
    }

    public Optional<String> getContentTypeParamsValue(String key) {
        return Optional.ofNullable(contentTypeParams.get(key));
    }

    public void setContentTypeParamsValue(String key, String value) {
        contentTypeParams.put(key, value);
    }

    public Map<String, Map<String, byte[]>> multipartBodyParams() {
        // body를 multipart로 나누어 Map<String, Map<String, byte[]>>으로 변환
        // this.getBody();
        String boundary = QueryParserUtil.getBoundaryFromContentType(getHeader().getValue("Content-Type"));
        if (boundary == null) {
            throw new IllegalArgumentException("Boundary not found in Content-Type header");
        }
        return QueryParserUtil.parseMulti(this.getBody(), boundary);
    }
}
