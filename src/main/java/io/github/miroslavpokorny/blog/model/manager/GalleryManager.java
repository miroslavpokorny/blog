package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;

import java.util.List;

public interface GalleryManager {
    List<Gallery> getAllGalleries();
    List<Gallery> getGalleriesByUserId(int id);
    Gallery createGallery(int authorId, String name, String description);
    Gallery createGallery(Gallery gallery);
    boolean addImagesToGallery(int galleryId, List<String> fileNames);
    Gallery getGalleryById(int id);
    List<GalleryItem> getAllGalleryItemsByGalleryId(int galleryId);
    Gallery updateGallery(int id, String name, String description);
    Gallery updateGallery(Gallery gallery);
    GalleryItem getGalleryItemById(int id);
    void deleteGalleryItemById(int id);
    void deleteGalleryById(int id);
    GalleryItem updateGalleryItem(int id, String title);
    GalleryItem updateGalleryItem(GalleryItem galleryItem);
}
