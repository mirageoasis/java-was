package dto;

import java.util.Map;

public record WriteRequestDto(
    String title,
    String content
) {
    public WriteRequestDto(Map<String, String> bodyParams) {
        this(bodyParams.get("title"), bodyParams.get("content"));
    }
}
