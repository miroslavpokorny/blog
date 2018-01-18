package io.github.miroslavpokorny.blog.controller.mvc;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.helper.ModelToViewModelMapper;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import io.github.miroslavpokorny.blog.model.manager.ArticleManager;
import io.github.miroslavpokorny.blog.model.manager.CategoryManager;
import io.github.miroslavpokorny.blog.model.viewmodel.CategoryViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryMvcController {
    private static final int ITEMS_PER_PAGE = 10;

    private final CategoryManager categoryManager;

    private final ArticleManager articleManager;

    @Autowired
    public CategoryMvcController(CategoryManager categoryManager, ArticleManager articleManager) {
        this.categoryManager = categoryManager;
        this.articleManager = articleManager;
    }

    @RequestMapping("/category/{categoryId}")
    public String categoryPage(Model model, @PathVariable int categoryId) {
        return categoryPage(model, categoryId, 1);
    }

    @RequestMapping("/category/{categoryId}/{page}")
    public String categoryPage(Model model, @PathVariable int categoryId, @PathVariable int page) {
        if (page < 1) {
            return "redirect:/category/" + categoryId;
        }
        List<Category> categories = categoryManager.getAllCategories();
        List<Category> filteredCategories = categories.stream().filter(category -> categoryId == category.getId()).collect(Collectors.toList());
        if (filteredCategories.size() == 0) {
            return "redirect:/";
        }
        Category currentCategory = filteredCategories.get(0);
        CategoryViewModel categoryViewModel = new CategoryViewModel();
        PaginationHelper<Article> pages = articleManager.getNewestArticlesInCategory(categoryId, page, ITEMS_PER_PAGE);
        if (page > 1 && pages.getItems().size() == 0) {
            return "redirect:/category/";
        }
        categoryViewModel.setCategories(categories.stream().map(ModelToViewModelMapper::categoryToCategoryInfoViewModel).collect(Collectors.toList()));
        categoryViewModel.setArticles(pages.getItems().stream().map(ModelToViewModelMapper::articleToArticleInfoViewModel).collect(Collectors.toList()));
        categoryViewModel.setCurrentCategory(ModelToViewModelMapper.categoryToCategoryInfoViewModel(currentCategory));
        categoryViewModel.setNumberOfPages(pages.getNumberOfPages());
        categoryViewModel.setPage(pages.getPage());
        model.addAttribute("viewModel", categoryViewModel);
        return "category";
    }
}
