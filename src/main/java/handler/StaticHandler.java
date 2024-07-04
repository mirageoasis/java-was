package handler;

import http.Header;
import http.Http;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import java.io.IOException;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import util.FileReader;

public class StaticHandler extends MyHandler {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(StaticHandler.class);
    private final Logger logger = Logger.getLogger(StaticHandler.class.getName());


    @Override
    void doGet(Http httpRequest, Http httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();

        if(requestLine.getUrlPath().getPath().equals("/register.html")) {
            logger.info("redirect to /registration/index.html");
            responseLine.redirect(httpRequest, httpResponse, "/registration/index.html");
            return;
        }

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());
        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());

        //httpResponse.;
        Header responseHeader = httpResponse.getHeader();

        responseLine.success();

        //Header
        responseHeader.addHeader("Content-Type", contentType);
        responseHeader.addHeader("Content-Length", String.valueOf(fileContent.length));

        httpResponse.setStartLine(responseLine);
        httpResponse.setBody(fileContent);
    }

    @Override
    void doPost(Http httpRequest, Http httpResponse) {
    }
}
