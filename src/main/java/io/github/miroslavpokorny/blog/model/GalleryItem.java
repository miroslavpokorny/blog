package io.github.miroslavpokorny.blog.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GalleryItem {
    @Id
    private int id;

    private String imageName;

    private String title;

    private Gallery gallery;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {
        this.gallery = gallery;
    }
}
