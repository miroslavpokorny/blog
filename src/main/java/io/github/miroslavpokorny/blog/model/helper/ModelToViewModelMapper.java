package io.github.miroslavpokorny.blog.model.helper;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.viewmodel.ArticleInfoViewModel;
import io.github.miroslavpokorny.blog.model.viewmodel.CategoryInfoViewModel;
import org.jsoup.Jsoup;

public class ModelToViewModelMapper {
    private static final int PREVIEW_CONTENT_LENGTH = 200;

    public static ArticleInfoViewModel articleToArticleInfoViewModel (Article article) {
        String previewContent = Jsoup.parse(article.getContent()).text();
        previewContent = previewContent.length() > PREVIEW_CONTENT_LENGTH ? previewContent.substring(0, PREVIEW_CONTENT_LENGTH - 3) + "..." : previewContent;
        ArticleInfoViewModel articleInfoViewModel = new ArticleInfoViewModel();
        articleInfoViewModel.setAuthor(article.getAuthor().getNickname());
        articleInfoViewModel.setName(article.getName());
        articleInfoViewModel.setPreviewImage(article.getPreviewImage());
        articleInfoViewModel.setPublishDate(article.getPublishDate());
        articleInfoViewModel.setContentPreview(previewContent);
        articleInfoViewModel.setId(article.getId());
        return articleInfoViewModel;
    }

    public static CategoryInfoViewModel categoryToCategoryInfoViewModel(Category category) {
        CategoryInfoViewModel categoryInfoViewModel = new CategoryInfoViewModel();
        categoryInfoViewModel.setId(category.getId());
        categoryInfoViewModel.setName(category.getName());
        return categoryInfoViewModel;
    }
}
