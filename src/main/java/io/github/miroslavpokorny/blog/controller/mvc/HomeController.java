package io.github.miroslavpokorny.blog.controller.mvc;

import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import io.github.miroslavpokorny.blog.model.manager.ArticleManager;
import io.github.miroslavpokorny.blog.model.viewmodel.ArticleInfoViewModel;
import io.github.miroslavpokorny.blog.model.viewmodel.HomeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final ArticleManager articleManager;

    private final int itemsPerPage = 10;

    @Autowired
    public HomeController(ArticleManager articleManager) {
        this.articleManager = articleManager;
    }

    @RequestMapping("/")
    public String homePage(Model model) {
        return homePage(1, model);
    }

    @RequestMapping("/{page}")
    public String homePage(@PathVariable int page, Model model) {
        if (page < 1) {
            return "redirect:/";
        }
        HomeViewModel homeViewModel = new HomeViewModel();
        PaginationHelper<Article> pages = articleManager.getNewestArticles(page, itemsPerPage);
        homeViewModel.setNumberOfPages(pages.getNumberOfPages());
        homeViewModel.setPage(page);
        homeViewModel.setLatestArticles(pages.getItems().stream().map(article -> {
            ArticleInfoViewModel articleInfoViewModel = new ArticleInfoViewModel();
            articleInfoViewModel.setAuthor(article.getAuthor().getNickname());
            articleInfoViewModel.setName(article.getName());
            articleInfoViewModel.setPreviewImage(article.getPreviewImage());
            articleInfoViewModel.setPublishDate(article.getPublishDate());
            return articleInfoViewModel;
        }).collect(Collectors.toList()));
        model.addAttribute("viewModel", homeViewModel);
        return "home";
    }
}
