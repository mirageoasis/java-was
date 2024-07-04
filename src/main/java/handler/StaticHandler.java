package handler;

import http.Header;
import http.Http;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import java.io.IOException;
import java.util.logging.Logger;
import util.FileReader;

public class StaticHandler extends MyHandler {

    private final Logger logger = Logger.getLogger(StaticHandler.class.getName());


    @Override
    void doGet(Http httpRequest, Http httpResponse) throws IOException {
        logger.info("StaticHandler doGet");
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());
        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());

        //httpResponse.;
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
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
