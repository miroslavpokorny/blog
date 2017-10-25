package io.github.miroslavpokorny.blog.model.dao;

import io.github.miroslavpokorny.blog.model.Gallery;

import java.util.List;

public interface IGalleryDao extends IDao<Gallery> {
    List<Gallery> getGalleriesByUserId(int id);
    List<Gallery> getAllGalleries();
    Gallery getGalleryById(int id);
}
