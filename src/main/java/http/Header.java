package http;

import java.util.HashMap;
import java.util.Map;

public class Header {

    private final Map<String, String> header;

    private Header(String headerString) {
        this.header = headerMapper(headerString);
    }

    public static Header fromString(String headerString) {
        return new Header(headerString);
    }

    private Map<String, String> headerMapper(String headerString) {
        if (headerString == null) {
            throw new IllegalArgumentException("헤더 문자열은 null일 수 없습니다.");
        }

        Map<String, String> headerMap = new HashMap<>();
        String[] splitHeaderLines = splitHeaderLine(headerString);
        for (String splitHeaderLine : splitHeaderLines) {
            processOneHeaderLine(headerMap, splitHeaderLine);
        }

        return headerMap;
    }

    private String[] splitHeaderLine(String headerString) {
        return headerString.split("\r\n");
    }

    private void processOneHeaderLine(Map<String, String> headerMap, String headerLine) {
        if (!headerLine.trim().isEmpty()) {
            String[] splitHeaderLine = splitOneHeaderLine(headerLine);
            String key = splitHeaderLine[0];
            String value = splitHeaderLine.length > 1 ? splitHeaderLine[1] : "";

            if (headerMap.containsKey(key)) {
                headerMap.put(key, headerMap.get(key) + ", " + value);
            } else {
                headerMap.put(key, value);
            }
        }
    }

    private String[] splitOneHeaderLine(String headerString) {
        if (headerString == null) {
            throw new IllegalArgumentException("null 값은 들어올 수 없습니다.");
        }

        String[] headerParts = headerString.split(":", 2);
        if (headerParts.length != 2) {
            throw new IllegalArgumentException("유효하지 않은 헤더 형식입니다: " + headerString);
        }

        String key = headerParts[0].trim();
        String value = headerParts[1].trim();

        if (key.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 헤더 형식입니다: " + headerString);
        }

        return new String[]{key, value};
    }

    public String getHeader(String key) {
        return header.getOrDefault(key, "");
    }
}
