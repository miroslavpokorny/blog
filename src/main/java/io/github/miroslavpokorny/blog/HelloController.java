package io.github.miroslavpokorny.blog;

import com.google.gson.Gson;
import io.github.miroslavpokorny.blog.model.Article;
import io.github.miroslavpokorny.blog.model.dao.*;
import io.github.miroslavpokorny.blog.model.helper.GsonHelper;
import io.github.miroslavpokorny.blog.model.helper.PaginationHelper;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    //TODO delete action

    @RequestMapping(value = "/temp", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String tempAction() {
        StringBuilder result = new StringBuilder();

        IUserDao userDao = new UserDao();
        IArticleDao articleDao = new ArticleDao();
        IArticleRatingDao articleRating = new ArticleRatingDao();
        ICategoryDao categoryDao = new CategoryDao();
        PaginationHelper<Article> page1 = articleDao.getArticlesSearch(1, 3 , "TEST");
        PaginationHelper<Article> page2 = articleDao.getArticlesSearch(2, 3 , "TEST");
        PaginationHelper<Article> page3 = articleDao.getArticlesSearch(3, 3 , "TEST---");
//        PaginationHelper<Article> page1 = articleDao.getNewestArticles(1, 3);
//        PaginationHelper<Article> page2 = articleDao.getNewestArticles(2, 3);
//        PaginationHelper<Article> page3 = articleDao.getNewestArticles(3, 3);

        Gson gson = GsonHelper.getGson();

        for(Article article : page1.getItems()) {
            article.getAuthor().setRole(null);
        }
        for(Article article : page2.getItems()) {
            article.getAuthor().setRole(null);
        }
        for(Article article : page3.getItems()) {
            article.getAuthor().setRole(null);
        }
        result.append("{\"page1\":");
        result.append(gson.toJson(page1));
        result.append(",\"page2\":");
        result.append(gson.toJson(page2));
        result.append(",\"page3\":");
        result.append(gson.toJson(page3));
        result.append("}");

        return result.toString();
//            List<User> users = userDao.getAll();
//            System.out.println(users);
//
//            Article article = new Article();
//            article.setAuthor(userDao.loadById(2));
//            article.setCategory(categoryDao.loadById(1));
//            article.setName("TEST_article");
//            article.setContent("TEST_article content");
//            article.setEditDate(new Date());
//            article.setPublishDate(new Date());
//            article.setPreviewImage("TEST_preview.png");
//            article.setVisible(true);
//
//            articleDao.create(article);
    }
}
