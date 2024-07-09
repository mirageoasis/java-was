package util;

import model.User;

public class RequestContext {
    private static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    private final String urlPath;
    private final User user;

    private RequestContext(String urlPath, User user) {
        this.urlPath = urlPath;
        this.user = user;
    }

    public static RequestContext of(String urlPath, User user) {
        RequestContext requestContext = new RequestContext(urlPath, user);
        threadLocal.set(requestContext);
        return requestContext;
    }

    public static RequestContext current() {
        return threadLocal.get();
    }

    public String getUrlPath() {
        return urlPath;
    }

    public User getUser() {
        return user;
    }
}
