package handler;

import http.HttpRequest;
import http.HttpResponse;
import http.ResponseValueSetter;
import repository.SessionManager;
import util.RequestContext;

public class LogoutHandler extends MyHandler {

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Session session = RequestContext.current().getSession();
        if (session != null) {
            SessionManager.getInstance().invalidateSession(session.getSessionId());
        }
        ResponseValueSetter.redirect(httpRequest, httpResponse, "/index.html");
    }
}
