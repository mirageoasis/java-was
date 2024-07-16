package handler;

import static util.HeaderStringUtil.CONTENT_TYPE;

import http.Header;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import http.startline.RequestLine;
import java.io.IOException;
import java.util.Optional;
import model.Article;
import model.User;
import org.slf4j.Logger;
import repository.ArticleRepository;
import session.Session;
import util.FileReader;
import util.LoggerUtil;
import util.RequestContext;
import util.StaticPage;

public class StaticHandler extends MyHandler {

    private static final Logger logger = LoggerUtil.getLogger();


    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        Header responseHeader = httpResponse.getHeader();

        if (requestLine.getUrlPath().getPath().equals(StaticPage.indexPage)) {
            // context로 로그인 여부 확인
            callIndexPage(httpRequest, httpResponse);
            return;
        }

        if (requestLine.getUrlPath().getPath().equals("/register.html")) {
            logger.info("redirect to {}", StaticPage.registerPage);
            ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.registerPage);
            return;
        }

        byte[] fileContent = FileReader.readFileFromUrlPath(requestLine.getUrlPath());

        if(fileContent == null) {
            return;
        }

        String contentType = FileReader.guessContentTypeFromUrlPath(requestLine.getUrlPath());
        responseHeader.addKey(CONTENT_TYPE, contentType);

        //httpResponse.;
        ResponseValueSetter.success(httpResponse, fileContent);
    }

    private void callIndexPage(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // 1. index.html 불러오기
        byte[] fileContent = FileReader.readFileFromUrlPath(RequestContext.current().getUrlPath());
        String fileString = new String(fileContent);
        String contentType = FileReader.guessContentTypeFromUrlPath(
            RequestContext.current().getUrlPath());
        Header responseHeader = httpResponse.getHeader();
        responseHeader.addKey(CONTENT_TYPE, contentType);

        // 2. 로그인 여부 확인 -> 추가할 파일 불러오기
        String fileToAdd = stringToAdd();
        String articleToAdd = articleToAdd();

        logger.info("fileToAdd: {}", fileToAdd);

        // 3. 파일 합치기
        String combinedString = fileString.replace("{{header-menu}}", fileToAdd);
        combinedString = combinedString.replace("{{article-title-list}}", articleToAdd);

        // 4. response에 담기
        ResponseValueSetter.success(httpResponse, combinedString.getBytes());
    }

    private String articleToAdd() {
        ArticleRepository articleRepository = ArticleRepository.getInstance();

        // Retrieve all articles and format them into an HTML structure
        return articleRepository.getAllArticles().entrySet().stream()
            .map(entry -> formatArticleAsHtml(entry.getValue()))
            .reduce("", (a, b) -> a + b);
    }

    // Helper method to format a single article as HTML
    private String formatArticleAsHtml(Article article) {
        StringBuilder html = new StringBuilder();
        html.append("<div class=\"article\">");
        html.append("<h2>").append(article.getTitle()).append("</h2>");
        html.append("<p><strong>User ID:</strong> ").append(article.getUserId()).append("</p>");
        html.append("<p>").append(article.getContent()).append("</p>");
        if (article.getPhotoPath() != null && !article.getPhotoPath().isEmpty()) {
            html.append("<img src=\"").append(article.getPhotoPath()).append("\" alt=\"Article Photo\" />");
        }
        html.append("<hr>");
        html.append("</div>");
        return html.toString();
    }

    private String stringToAdd() throws IOException {
        String failedPath = "index/logout-menu.html";
        String successPath = "index/login-menu.html";
        Optional<User> user = RequestContext.current().getSession()
            .flatMap(session -> Optional.ofNullable(
                (User) session.getAttribute(Session.USER)
            ));

        logger.info("user: {}", user);
        if (user.isPresent()) {
            logger.info("logged in");
            String ret = new String(FileReader.readFileFromUrlPath(successPath));
            return ret.replace("{username}", user.get().getName());
        }

        logger.info("not logged in");
        return new String(FileReader.readFileFromUrlPath(failedPath));
    }
}
