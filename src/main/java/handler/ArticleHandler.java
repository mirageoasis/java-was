package handler;

import dto.WriteRequestDto;
import exception.BadRequestException;
import exception.InternalServerError;
import http.HttpMultiPartRequest;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.util.Map;
import java.util.UUID;
import model.Article;
import org.slf4j.Logger;
import repository.ArticleRepository;
import session.Session;
import util.LoggerUtil;
import util.PhotoReader;
import util.RequestContext;
import util.StaticPage;

public class ArticleHandler extends MyHandler {

    private static final Logger logger = LoggerUtil.getLogger();

    @Override
    public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        // login
        // 글 작성
        // body를 통해 글 정보를 가져온다.
        // 이후 해당 글을 작성한다.
        // 보안 로직은 앞에서 처리해줄 예정

        HttpMultiPartRequest httpMultiPartRequest = (HttpMultiPartRequest) httpRequest;
        Map<String, Map<String, byte[]>> bodyParams = httpMultiPartRequest.multipartBodyParams();

        String title = bodyParams.get("title").get("content").toString();
        String content = bodyParams.get("content").get("content").toString();
        byte[] photo = bodyParams.get("photo").get("content");
        Session session = RequestContext.current().getSession().orElse(null);

        if (
            bodyParams.get("title") == null ||
                bodyParams.get("content") == null ||
                bodyParams.get("photo") == null
        ) {
            ResponseValueSetter.failRedirect(httpResponse, new BadRequestException("맞는 형식으로 입력해주세요."));
            return;
        }

        if (session == null || session.getUser() == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("로그인이 필요합니다."));
            return;
        }

        if (!validateBodyParams(title, content, photo)) {
            ResponseValueSetter.failRedirect(httpResponse,
                new BadRequestException("맞는 형식으로 입력해주세요."));
            return;
        }

        String photoFileName =
            UUID.randomUUID() + "." + httpMultiPartRequest.getFileExtension("photo");
        String filePath;
        try {
            filePath = PhotoReader.savePhoto(photoFileName, photo);
        } catch (Exception e) {
            ResponseValueSetter.failRedirect(httpResponse, new InternalServerError("사진 저장에 실패했습니다."));
            logger.error("사진 저장에 실패했습니다. {}", e.getMessage());
            return;
        }

        // 주소 추가
        WriteRequestDto writeRequestDto = new WriteRequestDto(title, content, filePath);
        // 글 작성 로직

        Article article = writeRequestDto.toEntity(session.getUser());
        ArticleRepository.getInstance().save(article);

        ResponseValueSetter.redirect(httpRequest, httpResponse, StaticPage.indexPage);
    }

    private boolean validateBodyParams(
        String title,
        String content,
        byte[] photo
    ) {
        return title != null && !title.isEmpty() &&
            content != null && !content.isEmpty() &&
            photo != null && photo.length > 0;
    }
}
