package filter;

import java.util.Optional;
import session.Session;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import http.startline.UrlPath;
import java.io.IOException;
import org.slf4j.Logger;
import session.SessionManager;
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

        //context 설정
        Integer contextSessionId = getContextSessionId(cookieStringValue);
        UrlPath urlPath = ((RequestLine) httpRequest.getStartLine()).getUrlPath();

        Session session = SessionManager.getInstance().getSession(contextSessionId);
        logger.info("urlPath: {}, session: {}", urlPath, session);
        RequestContext.of(urlPath, session);

        // 필터 작동
        filterChain.doFilter(httpRequest, httpResponse);
    }


    private Integer getContextSessionId(String cookieStringValue) {
        Optional<String> sessionId = Optional.ofNullable(CookieUtil.getCookieValue(cookieStringValue, CookieUtil.sessionId));
        return sessionId.map(Integer::parseInt).orElse(null);
    }

    @Override
    public void destroy() {

    }
}
