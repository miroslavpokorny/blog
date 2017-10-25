package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Gallery;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class GalleryDao extends DaoBase<Gallery> implements IGalleryDao {
    @Override
    public List<Gallery> getGalleriesByUserId(int id) {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public List<Gallery> getAllGalleries() {
        // TODO implement
        throw new NotImplementedException();
    }

    @Override
    public Gallery getGalleryById(int id) {
        // TODO implement
        throw new NotImplementedException();
    }
}
