package io.github.miroslavpokorny.blog.model.dto;

import java.util.List;

public class ArticleListDto extends DtoBase {
    private List<ArticleDto> articles;

    public List<ArticleDto> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleDto> articles) {
        this.articles = articles;
    }
}
