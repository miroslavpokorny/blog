package io.github.miroslavpokorny.blog.controller.mvc;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import io.github.miroslavpokorny.blog.model.manager.ArticleManager;
import io.github.miroslavpokorny.blog.model.manager.CategoryManager;
import io.github.miroslavpokorny.blog.model.viewmodel.ArticleInfoViewModel;
import io.github.miroslavpokorny.blog.model.viewmodel.CategoryInfoViewModel;
import io.github.miroslavpokorny.blog.model.viewmodel.HomeViewModel;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeMvcController {

    private final Authentication authentication;

    private final ArticleManager articleManager;

    private final CategoryManager categoryManager;

    private static final int ITEMS_PER_PAGE = 10;
    private static final int PREVIEW_CONTENT_LENGTH = 200;

    @Autowired
    public HomeMvcController(Authentication authentication, ArticleManager articleManager, CategoryManager categoryManager) {
        this.authentication = authentication;
        this.articleManager = articleManager;
        this.categoryManager = categoryManager;
    }

    @RequestMapping("/")
    public String homePage(Model model, @CookieValue(value = "tokenId", required = false) String tokenId) {
        return homePage(1, model, tokenId);
    }

    @RequestMapping("/{page}")
    public String homePage(@PathVariable int page, Model model, @CookieValue(value = "tokenId", required = false) String tokenId) {
        if (page < 1) {
            return "redirect:/";
        }
        HomeViewModel homeViewModel = new HomeViewModel();
        PaginationHelper<Article> pages = articleManager.getNewestArticles(page, ITEMS_PER_PAGE);
        if (page > 1 && pages.getItems().size() == 0) {
            return "redirect:/";
        }
        List<Category> categories = categoryManager.getAllCategories();
        homeViewModel.setNumberOfPages(pages.getNumberOfPages());
        homeViewModel.setPage(page);

        homeViewModel.setLatestArticles(pages.getItems().stream().map(article -> {
            String previewContent = Jsoup.parse(article.getContent()).text();
            previewContent = previewContent.length() > PREVIEW_CONTENT_LENGTH ? previewContent.substring(0, PREVIEW_CONTENT_LENGTH - 3) + "..." : previewContent;
            ArticleInfoViewModel articleInfoViewModel = new ArticleInfoViewModel();
            articleInfoViewModel.setAuthor(article.getAuthor().getNickname());
            articleInfoViewModel.setName(article.getName());
            articleInfoViewModel.setPreviewImage(article.getPreviewImage());
            articleInfoViewModel.setPublishDate(article.getPublishDate());
            articleInfoViewModel.setContentPreview(previewContent);
            return articleInfoViewModel;
        }).collect(Collectors.toList()));
        homeViewModel.setCategories(categories.stream().map(category -> {
            CategoryInfoViewModel categoryInfoViewModel = new CategoryInfoViewModel();
            categoryInfoViewModel.setId(category.getId());
            categoryInfoViewModel.setName(category.getName());
            return categoryInfoViewModel;
        }).collect(Collectors.toList()));
        homeViewModel.setAuthenticated(authentication.isAuthenticate(tokenId));
        homeViewModel.setRole(authentication.isAuthenticate(tokenId) ? authentication.getAuthenticatedUser(tokenId).getUser().getRole().getId() : 0);
        model.addAttribute("viewModel", homeViewModel);
        return "home";
    }
}
