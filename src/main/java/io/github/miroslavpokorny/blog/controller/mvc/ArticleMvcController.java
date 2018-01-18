package io.github.miroslavpokorny.blog.controller.mvc;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.Comment;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.error.ForbiddenError;
import io.github.miroslavpokorny.blog.model.error.NotFoundError;
import io.github.miroslavpokorny.blog.model.error.UnauthorizedError;
import io.github.miroslavpokorny.blog.model.form.CommentForm;
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

    @RequestMapping(value = "/article/{id}", method = RequestMethod.GET)
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
        model.addAttribute("commentForm", new CommentForm());
        return "article";
    }

    @RequestMapping(value = "/article/{id}", method = RequestMethod.POST)
    public String addArticleComment(@PathVariable("id") int id, @CookieValue(value = "tokenId", required = false) String tokenId, CommentForm commentForm) {
        if (!authentication.isAuthenticate(tokenId)) {
            throw new UnauthorizedError("You must sign in before you can add comment!");
        }
        if (!authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.USER)) {
            throw new ForbiddenError("You don't have permission to add comment!");
        }
        Article article = articleManager.getArticleById(id);
        if (article == null) {
            throw new NotFoundError("Article was not found!");
        }
        commentManager.createComment(id, authentication.getAuthenticatedUser(tokenId).getUser().getId(), commentForm.getComment(), true);
        return "redirect:/article/" + id;
    }

    @ExceptionHandler(NotFoundError.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public String notFoundHandle(Exception ex, Model model) {
        return handleError(ex, model, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedError.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String unauthorizedHandle(Exception ex, Model model) {
        return handleError(ex, model, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenError.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public String forbiddenHandle(Exception ex, Model model) {
        return handleError(ex, model, HttpStatus.FORBIDDEN);
    }

    private String handleError(Exception ex, Model model, HttpStatus status) {
        ErrorViewModel errorViewModel = new ErrorViewModel();
        errorViewModel.setCode(status.value() + " - " + status.getReasonPhrase());
        errorViewModel.setMessage(ex.getMessage());
        model.addAttribute("viewModel", errorViewModel);
        return "error";
    }
}
