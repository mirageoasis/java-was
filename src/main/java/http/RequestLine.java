package http;

public class RequestLine {
    private final HttpMethod method;
    private final String path;
    private final String version;

    public RequestLine(HttpMethod method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public static RequestLine fromRequest(String requestLine) {
        String[] split = requestLine.split(" ");
        if(split.length != 3) {
            throw new IllegalArgumentException("Request Line 형식이 잘못되었습니다.");
        }

        HttpMethod method = HttpMethod.of(split[0]);
        String path = split[1];
        String version = split[2];
        return new RequestLine(method, path, version);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

}