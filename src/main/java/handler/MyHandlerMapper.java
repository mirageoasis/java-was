package handler;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import util.LoggerUtil;

public class MyHandlerMapper {
    private static final Logger logger = LoggerUtil.getLogger();
    private static final MyHandlerMapper instance = new MyHandlerMapper();
    private final Map<String, MyHandler> handlerMap = new HashMap<>();

    private MyHandlerMapper() {
        addHandler("/index.html", new StaticHandler());
        addHandler("/create", new UserCreateHandler());
    }

    public static MyHandlerMapper getInstance() {
        if (instance == null) {
            return new MyHandlerMapper();
        }
        return instance;
    }

    private void addHandler(String urlPath, MyHandler handler) {
        handlerMap.put(urlPath, handler);
    }

    public MyHandler findHandler(String urlPath) {
        // TODO: 여기에 함수 하나 더 둬서 urlPath를 받아서 처리하도록 하자.

        if (urlPath == null) {
            logger.info("urlPath is null");
            return new StaticHandler();
        }

        if (handlerMap.getOrDefault(urlPath, null) == null) {
            logger.info("handler not found");
            return new StaticHandler();
        }

        logger.info("urlPath: " + urlPath);

        return handlerMap.get(urlPath);
    }
}
