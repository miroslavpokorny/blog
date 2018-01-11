package io.github.miroslavpokorny.blog.model.json;

import java.util.List;

public class GalleryListDto extends JsonBase {
    private List<GalleryDto> galleries;

    public List<GalleryDto> getGalleries() {
        return galleries;
    }

    public void setGalleries(List<GalleryDto> galleries) {
        this.galleries = galleries;
    }
}
