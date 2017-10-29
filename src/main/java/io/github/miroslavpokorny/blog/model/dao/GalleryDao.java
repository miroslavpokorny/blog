package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Gallery;

import java.util.List;

public interface GalleryDao extends Dao<Gallery> {
    List<Gallery> getGalleriesByUserId(int id);
    List<Gallery> getAllGalleries();
    Gallery getGalleryById(int id);
}
