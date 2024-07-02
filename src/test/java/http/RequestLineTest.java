package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RequestLineTest {

    @Test
    public void testFromRequest_ValidRequestLine() {
        String requestLine = "GET /index.html HTTP/1.1";
        RequestLine startLine = RequestLine.fromRequest(requestLine);

        assertEquals(HttpMethod.GET, startLine.getMethod());
        assertEquals("/index.html", startLine.getPath());
        assertEquals("HTTP/1.1", startLine.getVersion());
    }

    @Test
    public void testFromRequest_InvalidRequestLine() {
        String requestLine = "GET /index.html";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            RequestLine.fromRequest(requestLine);
        });

        assertEquals("Request Line 형식이 잘못되었습니다.", exception.getMessage());
    }

    @Test
    public void testGetMethod() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", "HTTP/1.1");
        assertEquals(HttpMethod.GET, requestLine.getMethod());
    }

    @Test
    public void testGetPath() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", "HTTP/1.1");
        assertEquals("/index.html", requestLine.getPath());
    }

    @Test
    public void testGetVersion() {
        RequestLine requestLine = new RequestLine(HttpMethod.GET, "/index.html", "HTTP/1.1");
        assertEquals("HTTP/1.1", requestLine.getVersion());
    }

}