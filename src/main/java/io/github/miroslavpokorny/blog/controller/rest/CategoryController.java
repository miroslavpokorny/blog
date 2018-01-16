package io.github.miroslavpokorny.blog.controller.rest;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.Category;
import io.github.miroslavpokorny.blog.model.error.NameAlreadyExistsException;
import io.github.miroslavpokorny.blog.model.dto.*;
import io.github.miroslavpokorny.blog.model.manager.CategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class CategoryController extends AuthorizeController {
    private final Authentication authentication;

    private final CategoryManager categoryManager;

    @Autowired
    public CategoryController(Authentication authentication, CategoryManager categoryManager) {
        this.authentication = authentication;
        this.categoryManager = categoryManager;
    }

    @RequestMapping("/api/category/list")
    public ResponseEntity getUsersList(@RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        List<Category> categories = this.categoryManager.getAllCategories();
        CategoryListDto json = new CategoryListDto();
        json.setCategories(categories.stream().map(category -> {
            CategoryDto cat = new CategoryDto();
            cat.setDescription(category.getDescription());
            cat.setId(category.getId());
            cat.setName(category.getName());
            return cat;
        }).collect(Collectors.toList()));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping("/api/category/add")
    public ResponseEntity addCategory(@RequestBody AddCategoryDto addCategoryDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        Category category = new Category();
        category.setDescription(addCategoryDto.getDescription());
        category.setName(addCategoryDto.getName());
        try {
            categoryManager.createCategory(category);
        } catch (NameAlreadyExistsException exception) {
            return conflictResponse(exception.getMessage());
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/category/remove")
    public ResponseEntity addCategory(@RequestBody RequestByIdDto requestById, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        categoryManager.deleteCategoryByID(requestById.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/category/edit")
    public ResponseEntity addCategory(@RequestBody CategoryDto categoryDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        Category category = categoryManager.getCategoryById(categoryDto.getId());
        if (category == null) {
            return notFoundResponse("Category not found!");
        }
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        try {
            categoryManager.updateCategory(category);
        } catch (NameAlreadyExistsException exception) {
            return conflictResponse(exception.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean isAccessForbidden(String tokenId) {
        return !this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.MODERATOR);
    }
}
