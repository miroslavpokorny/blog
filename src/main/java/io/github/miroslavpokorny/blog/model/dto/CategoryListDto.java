package io.github.miroslavpokorny.blog.model.dto;

import java.util.List;

public class CategoryListDto extends DtoBase {
    private List<CategoryDto> categories;

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
