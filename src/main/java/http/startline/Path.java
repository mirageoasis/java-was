package http.startline;

import java.util.HashMap;
import java.util.Map;

public class Path {

    public static Path of(String fullPath) {
        return new Path(fullPath);
    }

    private String fullPath;
    private String path;
    private Map<String, String> queryParameters;

    public Path(String fullPath) {
        this.fullPath = fullPath;
        this.queryParameters = new HashMap<>();
        parsePath();
    }

    private void parsePath() {
        String[] parts = fullPath.split("\\?", 2);
        this.path = parts[0];

        if (parts.length > 1) {
            String queryString = parts[1];
            parseQueryParameters(queryString);
        }
    }

    private void parseQueryParameters(String queryString) {
        String[] params = queryString.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                queryParameters.put(keyValue[0], keyValue[1]);
            } else {
                queryParameters.put(keyValue[0], "");
            }
        }
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public String getQueryParameter(String key) {
        return queryParameters.get(key);
    }
}
