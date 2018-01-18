package io.github.miroslavpokorny.blog.controller.mvc;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.Comment;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.error.NotFoundError;
import io.github.miroslavpokorny.blog.model.helper.ModelToViewModelMapper;
import io.github.miroslavpokorny.blog.model.manager.ArticleManager;
import io.github.miroslavpokorny.blog.model.manager.CategoryManager;
import io.github.miroslavpokorny.blog.model.manager.CommentManager;
import io.github.miroslavpokorny.blog.model.manager.GalleryManager;
import io.github.miroslavpokorny.blog.model.viewmodel.ArticleViewModel;
import io.github.miroslavpokorny.blog.model.viewmodel.ErrorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ArticleMvcController {
    private final ArticleManager articleManager;
    private final CategoryManager categoryManager;
    private final CommentManager commentManager;
    private final GalleryManager galleryManager;
    private final Authentication authentication;

    @Autowired
    public ArticleMvcController(ArticleManager articleManager, CategoryManager categoryManager, CommentManager commentManager, GalleryManager galleryManager, Authentication authentication) {
        this.articleManager = articleManager;
        this.categoryManager = categoryManager;
        this.commentManager = commentManager;
        this.authentication = authentication;
        this.galleryManager = galleryManager;
    }

    @RequestMapping("/article/{id}")
    public String article(Model model, @PathVariable("id") int id, @CookieValue(value = "tokenId", required = false) String tokenId) {
        Article article = articleManager.getArticleById(id);
        if (article == null) {
            throw new NotFoundError("Article was not found!");
        }
        List<Category> categories = categoryManager.getAllCategories();
        List<Comment> comments = commentManager.getAllCommentsForArticleByArticleId(id);
        ArticleViewModel articleViewModel = new ArticleViewModel();
        if (article.getGallery() != null) {
            List<GalleryItem> galleryItems = galleryManager.getAllGalleryItemsByGalleryId(article.getGallery().getId());
            articleViewModel.setGalleryItems(galleryItems.stream().map(ModelToViewModelMapper::galleryItemToGalleryItemViewModel).collect(Collectors.toList()));
        }
        articleViewModel.setCategories(categories.stream().map(ModelToViewModelMapper::categoryToCategoryInfoViewModel).collect(Collectors.toList()));
        articleViewModel.setComments(comments.stream().map(ModelToViewModelMapper::commentToCommentViewModel).collect(Collectors.toList()));
        articleViewModel.setContent(article.getContent());
        articleViewModel.setName(article.getName());
        articleViewModel.setPreviewImage(article.getPreviewImage());
        articleViewModel.setId(article.getId());
        articleViewModel.setAuthenticated(authentication.isAuthenticate(tokenId));
        articleViewModel.setCategory(article.getCategory().getId());
        articleViewModel.setAuthor(article.getAuthor().getNickname());
        articleViewModel.setPublishDate(article.getPublishDate());
        if (articleViewModel.isAuthenticated()) {
            articleViewModel.setRole(authentication.getAuthenticatedUser(tokenId).getUser().getRole().getId());
        }
        model.addAttribute("viewModel", articleViewModel);
        return "article";
    }

    @ExceptionHandler(NotFoundError.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundHandle(Exception ex, Model model) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.setCode("404");
        errorViewModel.setMessage(ex.getMessage());
        model.addAttribute("viewModel", errorViewModel);
        return "error";
    }
}
