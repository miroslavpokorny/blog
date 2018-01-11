package io.github.miroslavpokorny.blog.model.dto;

import java.util.List;

public class GalleryListDto extends DtoBase {
    private List<GalleryDto> galleries;

    public List<GalleryDto> getGalleries() {
        return galleries;
    }

    public void setGalleries(List<GalleryDto> galleries) {
        this.galleries = galleries;
    }
}
