package handler;

import dto.UserDto;
import http.Http;
import http.startline.RequestLine;
import http.startline.ResponseLine;
import java.util.Map;
import service.UserService;

public class UserCreateHandler extends MyHandler {

    private final UserService userService;

    public UserCreateHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    void doGet(Http httpRequest, Http httpResponse) {
        httpResponse.setBody("Hello World!\n".getBytes());
        RequestLine startLine = (RequestLine) httpRequest.getStartLine();
        Map<String, String> queryParams = startLine.getUrlPath().getQueryParameters();
        UserDto userDto = new UserDto(
            queryParams.get("userId"),
            queryParams.get("password"),
            queryParams.get("name"),
            queryParams.get("email")
        );
        userService.createUser(userDto);

        ResponseLine responseLine = (ResponseLine) httpResponse.getStartLine();




        responseLine.success();

        httpResponse.getHeader().addHeader("Content-Type", "text/html");
        httpResponse.getHeader().addHeader("Content-Length", String.valueOf(httpResponse.getBody().length));
    }

    @Override
    void doPost(Http httpRequest, Http httpResponse) {

    }
}
