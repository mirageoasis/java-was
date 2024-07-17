package repository;

import java.util.Map;
import java.util.Optional;
import model.Article;

public class ArticleRepositoryDB extends ArticleRepository{

    @Override
    void save(Article article) {

    }

    @Override
    Optional<Article> findById(Long id) {
        return Optional.empty();
    }

    @Override
    Map<Long, Article> getAllArticles() {
        return Map.of();
    }
}
