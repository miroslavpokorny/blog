package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;

import java.util.List;

public interface GalleryManager {
    List<Gallery> getAllGalleries();
    List<Gallery> getGalleriesByUserId(int id);
    Gallery createGallery(int authorId, String name, String description);
    Gallery createGallery(Gallery gallery);
    void addImagesToGallery(int galleryId, List<String> fileNames);
    Gallery getGalleryById(int id);
    List<GalleryItem> getAllGalleryItemsByGalleryId(int galleryId);
    void updateGallery(int id, String name, String description);
    void updateGallery(Gallery gallery);
    GalleryItem getGalleryItemById(int id);
    void deleteGalleryItemById(int id);
    void deleteGalleryById(int id);
    void updateGalleryItem(int id, String title);
    void updateGalleryItem(GalleryItem galleryItem);
}
