package handler;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import model.User;
import org.slf4j.Logger;
import session.Session;
import util.FileReader;
import util.LoggerUtil;
import util.RequestContext;

public class StaticHandler extends MyHandler {

    private static final Logger logger = LoggerUtil.getLogger();


    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        if (requestLine.getUrlPath().getPath().equals("/index.html")) {
            // context로 로그인 여부 확인
            indexPage(httpRequest, httpResponse);
            return;
        }

        if (requestLine.getUrlPath().getPath().equals("/register.html")) {
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

    private void indexPage(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // 1. index.html 불러오기
        byte[] fileContent = FileReader.readFileFromUrlPath(RequestContext.current().getUrlPath());
        String fileString = new String(fileContent);
        String contentType = FileReader.guessContentTypeFromUrlPath(
            RequestContext.current().getUrlPath());
        Header responseHeader = httpResponse.getHeader();
        responseHeader.addKey("Content-Type", contentType);

        // 2. 로그인 여부 확인 -> 추가할 파일 불러오기
        String fileToAdd = stringToAdd();

        logger.info("fileToAdd: {}", fileToAdd);

        // 3. 파일 합치기
        String combinedString = fileString.replace("{header-menu}", fileToAdd);

        // 4. response에 담기
        ResponseValueSetter.success(httpResponse, combinedString.getBytes());
    }

    private String stringToAdd() throws IOException {
        String failedPath = "index/logout-menu.html";
        String successPath = "index/login-menu.html";
        Session session = RequestContext.current().getSession();
        User user = (session != null) ? (User) session.getAttribute(Session.USER) : null;

        logger.info("user: {}", user);
        if (user != null) {
            logger.info("logged in");
            String ret = new String(FileReader.readFileFromUrlPath(successPath));
            return ret.replace("{username}", user.getName());
        }

        logger.info("not logged in");
        return new String(FileReader.readFileFromUrlPath(failedPath));
    }
}
