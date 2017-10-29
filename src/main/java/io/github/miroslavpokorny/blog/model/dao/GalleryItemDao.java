package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.GalleryItem;

import java.util.List;

public interface GalleryItemDao extends Dao<GalleryItem> {
    void addImagesToGallery(List<GalleryItem> items);
    List<GalleryItem> getAllItemsInGallery(int galleryId);
    GalleryItem getGalleryItemById(int id);
}
