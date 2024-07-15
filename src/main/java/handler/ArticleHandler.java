package handler;

import dto.WriteRequestDto;
import exception.BadRequestException;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import model.Article;
import repository.ArticleRepository;
import session.Session;
import util.RequestContext;

public class ArticleHandler extends MyHandler {
    @Override
    void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        // login
        // 글 가져오기
        // queryparamter를 통해 글 번호를 가져온다.
        // 이후 해당 글과 댓글 정보를 가져온다.
        return ;
    }

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        // login
        // 글 작성
        // body를 통해 글 정보를 가져온다.
        // 이후 해당 글을 작성한다.
        // 보안 로직은 앞에서 처리해줄 예정
        String title = httpRequest.getBodyParams().get("title");
        String content = httpRequest.getBodyParams().get("content");
        Session session = RequestContext.current().getSession().orElse(null);

        if (session == null  || session.getUser() == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("로그인이 필요합니다."));
            return;
        }

        if(title == null || content == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("제목과 내용을 입력해주세요."));
            return;
        }

        WriteRequestDto writeRequestDto = new WriteRequestDto(title, content);
        // 글 작성 로직

        Article article = writeRequestDto.toEntity(session.getUser());
        ArticleRepository.getInstance().save(article);

        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
