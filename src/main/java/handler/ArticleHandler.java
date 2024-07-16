package handler;

import dto.WriteRequestDto;
import exception.BadRequestException;
import http.HttpMultiPartRequest;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import model.Article;
import repository.ArticleRepository;
import session.Session;
import util.RequestContext;

public class ArticleHandler extends MyHandler {

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        // login
        // 글 작성
        // body를 통해 글 정보를 가져온다.
        // 이후 해당 글을 작성한다.
        // 보안 로직은 앞에서 처리해줄 예정

        Map<String, Map<String, byte[]>> bodyParams = ((HttpMultiPartRequest) httpRequest).multipartBodyParams();

        String title = bodyParams.get("title").get("content").toString();
        String content = bodyParams.get("content").get("content").toString();
        byte[] photo = bodyParams.get("photo").get("content");

        // photo 저장로직
        // photo를 저장하고 해당 경로를 저장한다.

        // Generate a unique file name for the photo
        String photoFileName = "dingding.ico";
        // Define the path where the photo will be saved
        Path photoPath = Paths.get(photoFileName);

        // Save the photo to the server
        try (FileOutputStream fos = new FileOutputStream(photoPath.toString())) {
            fos.write(photo);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file saving error
        }

        Session session = RequestContext.current().getSession().orElse(null);

        if (session == null || session.getUser() == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("로그인이 필요합니다."));
            return;
        }

        if (title == null || content == null || title.isEmpty() || content.isEmpty()) {
            ResponseValueSetter.failRedirect(httpResponse,
                new BadRequestException("제목과 내용을 입력해주세요."));
            return;
        }

        WriteRequestDto writeRequestDto = new WriteRequestDto(title, content);
        // 글 작성 로직

        Article article = writeRequestDto.toEntity(session.getUser());
        ArticleRepository.getInstance().save(article);

        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
