package util;

import handler.Session;

public class RequestContext {
    private static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    private final String urlPath;
    private final Session session;

    private RequestContext(String urlPath, Session session) {
        this.urlPath = urlPath;
        this.session = session;
    }

    public static RequestContext of(String urlPath, Session session) {
        RequestContext requestContext = new RequestContext(urlPath, session);
        threadLocal.set(requestContext);
        return requestContext;
    }

    public static RequestContext current() {
        return threadLocal.get();
    }

    public String getUrlPath() {
        return urlPath;
    }

    public Session getSession() {
        // return SessionManager.getInstance().getSession(this.sessionId);
        return session;
    }
}
