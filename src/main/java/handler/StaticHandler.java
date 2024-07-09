package handler;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import java.util.logging.Logger;
import util.FileReader;

public class StaticHandler extends MyHandler {
    private static final Logger logger = Logger.getLogger(StaticHandler.class.getName());


    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        Header responseHeader = httpResponse.getHeader();

//        if(requestLine.getUrlPath().getPath().equals("/index.html")) {
//            // context로 로그인 여부 확인
//            //ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
//            return;
//        }

        if(requestLine.getUrlPath().getPath().equals("/register.html")) {
            logger.info("redirect to /registration/index.html");
            ResponseValueSetter.redirect(httpRequest, httpResponse, "/registration/index.html");
            return;
        }

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());
        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());
        responseHeader.addKey("Content-Type", contentType);


        //httpResponse.;
        ResponseValueSetter.success(httpResponse, fileContent);
    }
}
