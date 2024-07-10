package filter;

import handler.MyHandler;
import handler.MyHandlerMapper;
import http.HttpRequest;
import http.HttpResponse;
import http.startline.RequestLine;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import util.LoggerUtil;
import util.RequestContext;

public class FilterChain {

    private static final Logger logger = LoggerUtil.getLogger();
    // filter의 이름과 filter 객체를 매핑
    private final Map<Pattern, Filter> filterMapping;

    // filter를 순서대로 넣는 리스트
    private final List<Filter> filters;

    //principal과 같은 객체는 overloading을 사용해서 작성해줌

    // handler
    private final MyHandlerMapper handlerMapper;

    // count
    private int count = 0;

    public FilterChain() {
        this.filterMapping = new HashMap<>(
            Map.of(
                Pattern.compile(".*"), new LoginFilter()
            )
        );
        this.filters = List.of(
            new LoginFilter()
        );
        this.handlerMapper = MyHandlerMapper.getInstance();
    }


    public void doFilter(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        // filter 순서대로 실행
        if (count < filters.size()) {
            Filter filter = filters.get(count);
            // TODO: filterMapping을 사용해서 url과 맞는 기능만 doFilter를 소환
            count++;
            filter.doFilter(httpRequest, httpResponse, this);
            return;
        }

        // handler 매핑하기
        if (RequestContext.current().getSession() != null){
            logger.info("session id: {}", RequestContext.current().getSession().getSessionId());
        }

        MyHandlerMapper handlerMapper = MyHandlerMapper.getInstance();
        RequestLine requestLine = (RequestLine) httpRequest.getStartLine();
        MyHandler chosenHandler = handlerMapper.findHandler(requestLine.getUrlPath().getPath());

        // handler 실행
        chosenHandler.handle(httpRequest, httpResponse);
    }


}
