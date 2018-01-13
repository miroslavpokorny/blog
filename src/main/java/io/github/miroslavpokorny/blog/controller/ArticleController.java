package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.dto.*;
import io.github.miroslavpokorny.blog.model.helper.ResourceHelper;
import io.github.miroslavpokorny.blog.model.manager.ArticleManager;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ArticleController extends AuthorizeController {
    private final Authentication authentication;

    private final ArticleManager articleManager;

    @Autowired
    public ArticleController(Authentication authentication, ArticleManager articleManager) {
        this.authentication = authentication;
        this.articleManager = articleManager;
    }

    @RequestMapping("/api/article/list")
    public ResponseEntity getArticlesList(@RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        List<Article> articles = articleManager.getAllArticles();
        ArticleListDto dto = new ArticleListDto();
        dto.setArticles(articles.stream().map(article -> {
            ArticleDto art = new ArticleDto();
            UserInfoDto user = new UserInfoDto();
            user.setEmail(article.getAuthor().getEmail());
            user.setEnabled(article.getAuthor().isEnabled());
            user.setId(article.getAuthor().getId());
            user.setName(article.getAuthor().getName());
            user.setNickname(article.getAuthor().getNickname());
            user.setSurname(article.getAuthor().getSurname());
            user.setRole(article.getAuthor().getRole().getId());
            art.setAuthor(user);
            art.setCategoryId(article.getCategory().getId());
            art.setContent(article.getContent());
            art.setId(article.getId());
            art.setName(article.getName());
            art.setPreviewImage(article.getPreviewImage());
            if (article.getGallery() != null) {
                art.setGalleryId(article.getGallery().getId());
            }
            return art;
        }).collect(Collectors.toList()));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping("/api/article/remove")
    public ResponseEntity removeArticle(@RequestBody RequestByIdDto articleId, @RequestParam(value = "tokenId", required = true) String tokenId, HttpServletRequest request) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        Article article = articleManager.getArticleById(articleId.getId());
        if (article == null) {
            return notFoundResponse("Article was not found!");
        }
        ResourceHelper.removeImageResourceIfExist(request, article.getPreviewImage());
        articleManager.deleteArticleById(articleId.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/article/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity addArticle (
            @RequestPart(value = "json") String article,
            @RequestPart("file") MultipartFile previewFile,
            @RequestParam(value = "tokenId", required = true) String tokenId, HttpServletRequest request) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        if (previewFile.isEmpty()) {
            return badRequestResponse("File was not uploaded!");
        }
        String previewImage = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(previewFile.getOriginalFilename());
        String resourceDir = ResourceHelper.getImagesResourceDir(request);
        ResourceHelper.createDirsIfNotExist(resourceDir);
        ResourceHelper.copyFile(previewFile, resourceDir + previewImage);
//        articleManager.createArticle(
//                addArticle.getName(),
//                addArticle.getContent(),
//                addArticle.getAuthorId(),
//                addArticle.getCategoryId(),
//                true,
//                previewImage,
//                addArticle.getGalleryId()
//        );
        return new ResponseEntity(HttpStatus.OK);
    }
//    ArticleEdit = "/api/article/edit",

    private boolean isAccessForbidden(String tokenId) {
        return !this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.EDITOR);
    }
}
