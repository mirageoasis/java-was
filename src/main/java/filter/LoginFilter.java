package filter;

import handler.Session;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.UrlPath;
import java.io.IOException;
import org.slf4j.Logger;
import repository.SessionManager;
import util.CookieUtil;
import util.LoggerUtil;
import util.RequestContext;

public class LoginFilter implements Filter {
    private static final Logger logger = LoggerUtil.getLogger();


    @Override
    public void init() {

    }

    @Override
    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse,
        FilterChain filterChain) throws IOException {
        // 여기서 쿠키를 확인한다.
        String cookieStringValue = httpRequest.getHeader().getValue("Cookie");
        String sessionId = CookieUtil.getCookieValue(cookieStringValue, CookieUtil.sessionId);

        //context 설정
        UrlPath urlPath = ((RequestLine) httpRequest.getStartLine()).getUrlPath();
        Integer contextSessionId = (sessionId != null) ? Integer.parseInt(sessionId) : null;
        Session session = SessionManager.getInstance().getSession(contextSessionId);
        logger.info("urlPath: {}, session: {}", urlPath, session);
        RequestContext.of(urlPath, session);

        // 필터 작동
        filterChain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {

    }
}
