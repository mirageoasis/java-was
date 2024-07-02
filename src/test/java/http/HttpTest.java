package http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import http.startline.RequestLine;
import http.startline.StartLine;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class HttpTest {

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideValidHttpRequests() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(
                "GET /index.html HTTP/1.1\r\nHost: www.example.com\r\nConnection: keep-alive\r\n\r\nbody content",
                "GET", "/index.html", "HTTP/1.1", "Host: www.example.com\r\nConnection: keep-alive", "body content"
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                "GET /index.html HTTP/1.1\r\nHost: www.example.com\r\nConnection: keep-alive\r\n\r\n",
                "GET", "/index.html", "HTTP/1.1", "Host: www.example.com\r\nConnection: keep-alive", ""
            ),
            org.junit.jupiter.params.provider.Arguments.of(
                "GET /index.html HTTP/1.1\r\n\r\nbody content",
                "GET", "/index.html", "HTTP/1.1", "", "body content"
            )
        );
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideInvalidHttpRequests() {
        return Stream.of(
            org.junit.jupiter.params.provider.Arguments.of(null, "HTTP 메시지는 null이거나 비어 있을 수 없습니다."),
            org.junit.jupiter.params.provider.Arguments.of("", "HTTP 메시지는 null이거나 비어 있을 수 없습니다."),
            org.junit.jupiter.params.provider.Arguments.of("\r\nHost: www.example.com\r\nConnection: keep-alive\r\n\r\nbody content", "잘못된 HTTP 메시지 형식: start-line이 없습니다.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidHttpRequests")
    @DisplayName("유효한 HTTP 요청 테스트")
    public void testStringToRequest_ValidRequest(String httpRequest, String expectedMethod, String expectedPath, String expectedVersion, String expectedHeaders, String expectedBody) {
        Http http = Http.stringToRequest(httpRequest);
        StartLine startLine = http.getStartLine();
        Header header = http.getHeader();
        String body = http.getBody();

        assertTrue(startLine instanceof RequestLine);
        RequestLine requestLine = (RequestLine) startLine;
        assertEquals(expectedMethod, requestLine.getMethod().name());
        assertEquals(expectedPath, requestLine.getPath());
        assertEquals(expectedVersion, requestLine.getVersion());
        assertEquals(expectedHeaders, header.toString());
        assertEquals(expectedBody, body);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidHttpRequests")
    @DisplayName("유효하지 않은 HTTP 요청 테스트")
    public void testStringToRequest_InvalidRequest(String httpRequest, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Http.stringToRequest(httpRequest);
        });
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
