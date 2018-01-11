package io.github.miroslavpokorny.blog.model.json;

import java.util.List;

public class CategoryListDto extends JsonBase {
    private List<CategoryDto> categories;

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
}
