package io.github.miroslavpokorny.blog.model.manager;

import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.dao.GalleryDao;
import io.github.miroslavpokorny.blog.model.dao.GalleryItemDao;
import io.github.miroslavpokorny.blog.model.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultGalleryManager implements GalleryManager {
    private final GalleryDao galleryDao;

    private final GalleryItemDao galleryItemDao;

    private final UserDao userDao;

    @Autowired
    public DefaultGalleryManager(GalleryDao galleryDao, GalleryItemDao galleryItemDao, UserDao userDao) {
        this.galleryDao = galleryDao;
        this.galleryItemDao = galleryItemDao;
        this.userDao = userDao;
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.getAllGalleries();
    }

    @Override
    public List<Gallery> getGalleriesByUserId(int id) {
        return galleryDao.getGalleriesByUserId(id);
    }

    @Override
    public Gallery createGallery(int authorId, String name, String description) {
        Gallery gallery = new Gallery();
        gallery.setAuthor(userDao.loadById(authorId));
        gallery.setName(name);
        gallery.setDescription(description);
        return createGallery(gallery);
    }

    @Override
    public Gallery createGallery(Gallery gallery) {
        return galleryDao.create(gallery);
    }

    @Override
    public void addImagesToGallery(int galleryId, List<String> fileNames) {
        List<GalleryItem> galleryItems = fileNames.stream().map((fileName) -> {
            GalleryItem galleryItem = new GalleryItem();
            galleryItem.setImageName(fileName);
            galleryItem.setGallery(galleryDao.loadById(galleryId));
            return galleryItem;
        }).collect(Collectors.toList());
        galleryItemDao.addImagesToGallery(galleryItems);
    }

    @Override
    public Gallery getGalleryById(int id) {
        return galleryDao.getGalleryById(id);
    }

    @Override
    public List<GalleryItem> getAllGalleryItemsByGalleryId(int galleryId) {
        return galleryItemDao.getAllItemsInGallery(galleryId);
    }

    @Override
    public void updateGallery(int id, String name, String description) {
        Gallery gallery = galleryDao.getById(id);
        gallery.setName(name);
        gallery.setDescription(description);
        updateGallery(gallery);
    }

    @Override
    public void updateGallery(Gallery gallery) {
        galleryDao.update(gallery);
    }

    @Override
    public GalleryItem getGalleryItemById(int id) {
        return galleryItemDao.getGalleryItemById(id);
    }

    @Override
    public void deleteGalleryItemById(int id) {
        galleryItemDao.delete(galleryItemDao.loadById(id));
    }

    @Override
    public void deleteGalleryById(int id) {
        galleryDao.delete(galleryDao.loadById(id));
    }

    @Override
    public void updateGalleryItem(int id, String title) {
        GalleryItem galleryItem = galleryItemDao.getById(id);
        galleryItem.setTitle(title);
        updateGalleryItem(galleryItem);
    }

    @Override
    public void updateGalleryItem(GalleryItem galleryItem) {
        galleryItemDao.update(galleryItem);
    }
}
