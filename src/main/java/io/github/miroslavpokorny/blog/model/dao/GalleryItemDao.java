package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.GalleryItem;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class GalleryItemDao extends  DaoBase<GalleryItem> implements IGalleryItemDao{
    @Override
    public boolean addImagesToGallery(List<GalleryItem> items) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<GalleryItem> getAllItemsForGallery(int galleryId) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public GalleryItem getGalleryItemById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
