package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class DefaultGalleryManager implements GalleryManager {
    @Override
    public List<Gallery> getAllGalleries() {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<Gallery> getGalleriesByUserId(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery createGallery(int authorId, String name, String description) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery createGallery(Gallery gallery) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public boolean addImagesToGallery(int galleryId, List<String> fileNames) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery getGalleryById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<GalleryItem> getAllGalleryItemsByGalleryId(int galleryId) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery updateGallery(int id, String name, String description) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery updateGallery(Gallery gallery) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public GalleryItem getGalleryItemById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public void deleteGalleryItemById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public void deleteGalleryById(int id) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public GalleryItem updateGalleryItem(int id, String title) {
        //TODO implement
        throw new NotImplementedException();
    }

    @Override
    public GalleryItem updateGalleryItem(GalleryItem galleryItem) {
        //TODO implement
        throw new NotImplementedException();
    }
}
