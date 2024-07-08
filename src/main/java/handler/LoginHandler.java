package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import repository.SessionRepository;
import repository.UserRepository;
import service.UserService;
import util.LoggerUtil;

public class LoginHandler extends MyHandler{
    private static final Logger logger = LoggerUtil.getLogger();
    private final UserService userService = new UserService(UserRepository.getInstance());
    private final SessionRepository sessionRepository = new SessionRepository();

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        logger.info("LoginHandler doPost");
        Map<String, String> bodyParams = httpRequest.getBodyParams();
        System.out.println(new String(httpRequest.getBody()));
        System.out.println(bodyParams);
        String userId = bodyParams.get("username");
        String password = bodyParams.get("password");
        logger.info("userId: {}, password: {}", userId, password);
        User user = userService.findUserById(userId);
        if (user == null || !user.getPassword().equals(password)) {
            logger.info("Login fail");
            ResponseValueSetter.redirect(httpRequest, httpResponse, "user/login_failed.html");
            return;
        }
        logger.info("Login success");
        Session session = new Session(user);
        Integer sessionId = sessionRepository.addSession(session);
        httpResponse.getHeader().addKey("Set-Cookie", "sid=" + sessionId + "; Path=/");
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
