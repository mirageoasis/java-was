package http;

import exception.GeneralException;
import http.startline.ResponseLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseWriter {

    public static final String CRLF = "\r\n";
    private static final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

    public static void success(HttpResponse httpResponse, byte[] body) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLine.setVersion("HTTP/1.1");
        responseLine.setStatusCode(200);
        responseLine.setStatusMessage("OK");

        // header
        responseHeader.addHeader("Content-Length", String.valueOf(body.length));

        // body
        httpResponse.setBody(body);
    }

    public static void success(HttpResponse httpResponse) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        // start line
        responseLine.setVersion("HTTP/1.1");
        responseLine.setStatusCode(200);
        responseLine.setStatusMessage("OK");

        // header
        responseHeader.addHeader("Content-Length", "0");

        // body
        httpResponse.setBody(new byte[0]);
    }

    public static void redirect(HttpRequest httpRequest, HttpResponse httpResponse, String urlPath) {
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        responseLine.setStatusCode(302);
        responseLine.setStatusMessage("Found");
        responseLine.setVersion("HTTP/1.1");

        Header responseHeader = httpResponse.getHeader();
        responseHeader.addHeader("Location", urlPath);

        httpResponse.setStartLine(responseLine);
    }

    public static void fail(HttpResponse httpResponse, GeneralException error) {
        logger.error("HTTP REQUEST ERROR: {}", error.getMessage());
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();
        // error의 코드를 받아온다.
        responseLine.setVersion("HTTP/1.1");
        responseLine.setStatusCode(error.getStatusCode());
        responseLine.setStatusMessage(error.getMessage());

        responseHeader.addHeader("Content-Length", "0");

        httpResponse.setBody(new byte[0]);
    }
}
