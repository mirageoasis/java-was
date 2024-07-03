package handler;

import http.Http;
import http.HttpMethod;
import http.startline.RequestLine;
import java.io.IOException;

abstract public class MyHandler {
    //

    public void handle(Http httpRequest, Http httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        HttpMethod httpMethod = requestLine.getMethod();

        if (httpMethod.equals(HttpMethod.GET)) {
            doGet(httpRequest, httpResponse);
        } else if (httpMethod.equals(HttpMethod.POST)) {
            doPost(httpRequest, httpResponse);
        }
    }

    abstract void doGet(Http httpRequest, Http httpResponse) throws IOException;
    abstract void doPost(Http httpRequest, Http httpResponse);
}
