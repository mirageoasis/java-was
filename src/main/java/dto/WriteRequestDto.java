package dto;

import model.Article;
import model.User;

public record WriteRequestDto(
    String title,
    String content
) {
    public WriteRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Article toEntity(User user) {
        return new Article(title, content, user.getUserId());
    }
}
