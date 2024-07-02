package http.startline;

import http.HttpMethod;

public class RequestLine extends StartLine {
    private final HttpMethod method;
    private final Path path;

    private RequestLine(HttpMethod method, Path path, String version) {
        super(version);
        this.method = method;
        this.path = path;
    }

    public static RequestLine fromString(String requestLine) {
        String[] split = requestLine.split(" ");
        if(split.length != 3) {
            throw new IllegalArgumentException("Request Line 형식이 잘못되었습니다.");
        }

        HttpMethod method = HttpMethod.of(split[0]);
        String path = split[1];
        String version = split[2];
        return new RequestLine(method, Path.of(path), version);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return method + " " + path + " " + getVersion();
    }

}
