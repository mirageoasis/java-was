package handler;

import http.HttpRequest;
import http.HttpResponse;

public class LogoutHandler extends MyHandler {

    @Override
    void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        // Integer sessionId = httpRequest.getHeader().getValue("Cookie").hashCode();

        // httpRequest.getSession().invalidate();
        // httpResponse.sendRedirect("/index.html");
    }
}
