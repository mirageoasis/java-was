package handler;

import http.Http;
import http.startline.ResponseLine;

public class UserCreateHandler extends MyHandler {


    @Override
    void doGet(Http httpRequest, Http httpResponse) {
        httpResponse.setBody("Hello World!\n".getBytes());

        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();
        responseLine.success();

        httpResponse.getHeader().addHeader("Content-Type", "text/html");
        httpResponse.getHeader().addHeader("Content-Length", String.valueOf(httpResponse.getBody().length));
    }

    @Override
    void doPost(Http httpRequest, Http httpResponse) {

    }
}
