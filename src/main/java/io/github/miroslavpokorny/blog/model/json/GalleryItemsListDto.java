package io.github.miroslavpokorny.blog.model.json;

import java.util.List;

public class GalleryItemsListDto extends JsonBase {
    private List<GalleryItemDto> items;

    public List<GalleryItemDto> getItems() {
        return items;
    }

    public void setItems(List<GalleryItemDto> items) {
        this.items = items;
    }
}

