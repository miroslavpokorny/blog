package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.GalleryItem;

import java.util.List;

public interface IGalleryItemDao extends IDao<GalleryItem> {
    boolean addImagesToGallery(List<GalleryItem> items);
    List<GalleryItem> getAllItemsForGallery(int galleryId);
    GalleryItem getGalleryItemById(int id);
}
