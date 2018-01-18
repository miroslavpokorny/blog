package io.github.miroslavpokorny.blog.model.helper;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.Comment;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.viewmodel.*;
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

    public static CommentViewModel commentToCommentViewModel(Comment comment) {
        CommentViewModel commentViewModel = new CommentViewModel();
        commentViewModel.setAuthor(comment.getAuthor().getNickname());
        commentViewModel.setComment(comment.getContent());
        commentViewModel.setId(comment.getId());
        commentViewModel.setPublishDate(comment.getPublishDate());
        return commentViewModel;
    }

    public static GalleryItemViewModel galleryItemToGalleryItemViewModel(GalleryItem galleryItem) {
        GalleryItemViewModel galleryItemViewModel = new GalleryItemViewModel();
        galleryItemViewModel.setImageName(galleryItem.getImageName());
        galleryItemViewModel.setTitle(galleryItem.getTitle());
        return galleryItemViewModel;
    }
}
