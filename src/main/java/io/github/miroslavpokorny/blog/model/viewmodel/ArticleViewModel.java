package io.github.miroslavpokorny.blog.model.viewmodel;

import java.util.Date;
import java.util.List;

public class ArticleViewModel extends BaseViewModel {
    private int id;
    private String name;
    private String content;
    private String previewImage;
    private List<GalleryItemViewModel> galleryItems;
    private List<CommentViewModel> comments;
    private List<CategoryInfoViewModel> categories;
    private int category;
    private String author;
    private Date publishDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(String previewImage) {
        this.previewImage = previewImage;
    }

    public List<GalleryItemViewModel> getGalleryItems() {
        return galleryItems;
    }

    public void setGalleryItems(List<GalleryItemViewModel> galleryItems) {
        this.galleryItems = galleryItems;
    }

    public List<CommentViewModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentViewModel> comments) {
        this.comments = comments;
    }

    public List<CategoryInfoViewModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInfoViewModel> categories) {
        this.categories = categories;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
