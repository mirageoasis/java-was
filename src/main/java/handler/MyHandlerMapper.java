package handler;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import repository.UserRepository;
import service.UserService;
import util.LoggerUtil;

public class MyHandlerMapper {
    private static final Logger logger = LoggerUtil.getLogger();
    private static final MyHandlerMapper instance = new MyHandlerMapper();
    private final Map<String, MyHandler> handlerMap = new HashMap<>();

    private MyHandlerMapper() {
        addHandler("/create", new UserCreateHandler(new UserService(new UserRepository())));
        addHandler("/registration", new RegistrationHandler());
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
        if (urlPath == null) {
            logger.error("urlPath is null");
            return null;
        }
        if (isStaticHandler(urlPath)){
            logger.info("StaticHandler");
            return new StaticHandler();
        }

        return handlerMap.get(urlPath);
    }

    private boolean isStaticHandler(String urlPath) {
        // '/' 이후에 '.'이 있으면 staticHandler로 간주
        int slashIndex = urlPath.lastIndexOf('/');
        if (slashIndex != -1) {
            String pathAfterSlash = urlPath.substring(slashIndex + 1);
            return pathAfterSlash.contains(".");
        }
        return false;
    }

}
