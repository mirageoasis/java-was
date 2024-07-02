package http;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HeaderTest {

    @ParameterizedTest
    @MethodSource("normalHeader")
    void 정상적인_헤더_생성(String normalHeaderString, String key, String value) {
        // given
        Header header = Header.fromString(normalHeaderString);

        // when
        String result = header.getHeader(key);

        // then
        assertEquals(value, result);
    }

    @Test
    @DisplayName("헤더 키가 없는 경우 빈 문자열을 반환한다.")
    void 헤더_키가_없는_경우() {
        // given
        Header header = Header.fromString("Host: localhost:8080");

        // when
        String result = header.getHeader("No-Exist");

        // then
        assertEquals("", result);
    }


    @ParameterizedTest
    @DisplayName("헤더의 형식이 잘못된 경우 예외를 던진다.")
    @MethodSource("invalidHeader")
    void 헤더_형식이_잘못된_경우(String invalidHeaderString) {
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            // when
            Header.fromString(invalidHeaderString);
        });
    }

    static Stream<Arguments> normalHeader() {
        return Stream.of(
            Arguments.of("Host: localhost:8080", "Host", "localhost:8080"),
            Arguments.of("Content-Type: application/json", "Content-Type", "application/json"),
            Arguments.of("Accept: */*", "Accept", "*/*"),
            Arguments.of("User-Agent: Mozilla/5.0", "User-Agent", "Mozilla/5.0"),
            Arguments.of("Cache-Control: no-cache", "Cache-Control", "no-cache"),
            Arguments.of("Authorization: Bearer token", "Authorization", "Bearer token"),
            Arguments.of("Referer: http://example.com", "Referer", "http://example.com"),
            Arguments.of("Connection: keep-alive", "Connection", "keep-alive"),
            Arguments.of("Content-Length: 1234", "Content-Length", "1234"),
            Arguments.of("Cookie: sessionId=abc123", "Cookie", "sessionId=abc123"),
            Arguments.of("Empty-Header: ", "Empty-Header", ""),
            Arguments.of("Host: localhost:8080\r\nContent-Type: application/json", "Content-Type", "application/json"),
            Arguments.of("Host: localhost:8080\r\nContent-Type: application/json", "Host", "localhost:8080"),
            Arguments.arguments("Multiple-Values: value1\r\nMultiple-Values: value2", "Multiple-Values", "value1, value2")
        );
    }

    static Stream<Arguments> invalidHeader() {
        return Stream.of(
            Arguments.of("InvalidHeaderFormat"),
            Arguments.of(": MissingKey"),
            Arguments.of("NoColonSeparator")
        );
    }
}