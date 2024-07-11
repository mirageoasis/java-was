package handler;

import static util.HeaderStringUtil.SET_COOKIE;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import session.SessionManager;
import util.RequestContext;

public class LogoutHandler extends MyHandler {

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        RequestContext.current().getSession().ifPresent(
            s -> SessionManager.getInstance().invalidateSession(s.getSessionId())
        );

        httpResponse.getHeader().addKey(SET_COOKIE, "sid=; Path=/; Max-Age=0");
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
