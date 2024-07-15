package handler;

import dto.WriteRequestDto;
import exception.BadRequestException;
import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;

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
        if(httpRequest.getBodyParams().get("title") == null || httpRequest.getBodyParams().get("content") == null) {
            ResponseValueSetter.fail(httpResponse, new BadRequestException("제목과 내용을 입력해주세요."));
        }

        WriteRequestDto writeRequestDto = new WriteRequestDto(httpRequest.getBodyParams());
        // 글 작성 로직



    }
}
