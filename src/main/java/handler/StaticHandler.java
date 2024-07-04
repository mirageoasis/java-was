package handler;

import http.Header;
import http.Http;
import http.HttpResponse;
import http.ResponseWriter;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import java.io.IOException;
import java.util.logging.Logger;
import util.FileReader;

public class StaticHandler extends MyHandler {
    private final Logger logger = Logger.getLogger(StaticHandler.class.getName());


    @Override
    void doGet(Http httpRequest, Http httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        if(requestLine.getUrlPath().getPath().equals("/register.html")) {
            logger.info("redirect to /registration/index.html");
            responseLine.redirect(httpRequest, httpResponse, "/registration/index.html");
            return;
        }

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());
        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());
        responseHeader.addHeader("Content-Type", contentType);


        //httpResponse.;
        ResponseWriter.success((HttpResponse) httpResponse, fileContent);
    }

    @Override
    void doPost(Http httpRequest, Http httpResponse) {
    }
}
