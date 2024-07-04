package handler;

import http.Http;
import http.HttpMethod;
import http.startline.RequestLine;
import java.io.IOException;
import util.LoggerUtil;
import org.slf4j.Logger;

abstract public class MyHandler {
    private static final Logger logger = LoggerUtil.getLogger();

    public void handle(Http httpRequest, Http httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        HttpMethod httpMethod = requestLine.getMethod();
        logger.info("Request: {}", httpRequest);
        if (httpMethod.equals(HttpMethod.GET)) {
            logger.info("GET");
            doGet(httpRequest, httpResponse);
        } else if (httpMethod.equals(HttpMethod.POST)) {
            logger.info("POST");
            doPost(httpRequest, httpResponse);
        }
    }

    abstract void doGet(Http httpRequest, Http httpResponse) throws IOException;
    abstract void doPost(Http httpRequest, Http httpResponse);
}
