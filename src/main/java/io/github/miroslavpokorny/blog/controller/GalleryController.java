package io.github.miroslavpokorny.blog.controller;

import io.github.miroslavpokorny.blog.authentication.Authentication;
import io.github.miroslavpokorny.blog.authentication.Role;
import io.github.miroslavpokorny.blog.model.Gallery;
import io.github.miroslavpokorny.blog.model.GalleryItem;
import io.github.miroslavpokorny.blog.model.User;
import io.github.miroslavpokorny.blog.model.json.*;
import io.github.miroslavpokorny.blog.model.manager.GalleryManager;
import io.github.miroslavpokorny.blog.model.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class GalleryController extends AuthorizeController {

    private final Authentication authentication;

    private final GalleryManager galleryManager;

    private final UserManager userManager;

    @Autowired
    public GalleryController(Authentication authentication, GalleryManager galleryManager, UserManager userManager) {
        this.authentication = authentication;
        this.galleryManager = galleryManager;
        this.userManager = userManager;
    }

    @RequestMapping("/api/gallery/list")
    public ResponseEntity getGalleriesList(@RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        List<Gallery> galleries = galleryManager.getAllGalleries();
        GalleryListDto json = new GalleryListDto();
        json.setGalleries(galleries.stream().map(gallery -> {
            GalleryDto gal = new GalleryDto();
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setRole(gallery.getAuthor().getRole().getId());
            userInfoDto.setSurname(gallery.getAuthor().getSurname());
            userInfoDto.setNickname(gallery.getAuthor().getNickname());
            userInfoDto.setEmail(gallery.getAuthor().getEmail());
            userInfoDto.setName(gallery.getAuthor().getName());
            userInfoDto.setId(gallery.getAuthor().getId());
            userInfoDto.setEnabled(gallery.getAuthor().isEnabled());
            gal.setAuthor(userInfoDto);
            gal.setDescription(gallery.getDescription());
            gal.setId(gallery.getId());
            gal.setName(gallery.getName());
            return gal;
        }).collect(Collectors.toList()));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/itemList")
    public ResponseEntity getGalleryItemsList(@RequestBody RequestByIdJson galleryId, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        Gallery gallery = galleryManager.getGalleryById(galleryId.getId());
        if (gallery == null) {
            return notFoundResponse("Gallery not found!");
        }
        List<GalleryItem> galleryItems = galleryManager.getAllGalleryItemsByGalleryId(galleryId.getId());
        GalleryItemsListDto json = new GalleryItemsListDto();
        json.setItems(galleryItems.stream().map(galleryItem -> {
            GalleryItemDto galItem = new GalleryItemDto();
            galItem.setGalleryId(galleryItem.getGallery().getId());
            galItem.setId(galleryItem.getId());
            galItem.setImageName(galleryItem.getImageName());
            galItem.setTitle(galleryItem.getTitle());
            return galItem;
        }).collect(Collectors.toList()));
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/add")
    public ResponseEntity addGallery(@RequestBody AddGalleryDto addGalleryDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        galleryManager.createGallery(addGalleryDto.getAuthorId(), addGalleryDto.getName(), addGalleryDto.getDescription());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/remove")
    public ResponseEntity removeGallery(@RequestBody RequestByIdJson galleryId, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        galleryManager.deleteGalleryById(galleryId.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/edit")
    public ResponseEntity editGallery(@RequestBody GalleryDto galleryDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        Gallery gallery = galleryManager.getGalleryById(galleryDto.getId());
        if (gallery == null) {
            return notFoundResponse("Gallery was not found!");
        }
        if (gallery.getAuthor().getId() != galleryDto.getAuthor().getId()) {
            User user = userManager.getUserById(galleryDto.getAuthor().getId());
            if (user == null) {
                return notFoundResponse("New author was not found!");
            }
            gallery.setAuthor(user);
        }
        gallery.setName(galleryDto.getName());
        gallery.setDescription(galleryDto.getDescription());
        galleryManager.updateGallery(gallery);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/itemRemove")
    public ResponseEntity removeGalleryItem(@RequestBody RequestByIdJson galleryItemId, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        galleryManager.deleteGalleryItemById(galleryItemId.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/api/gallery/itemEdit")
    public ResponseEntity editGalleryItem(@RequestBody GalleryItemDto galleryItemDto, @RequestParam(value = "tokenId", required = true) String tokenId) {
        if (!authentication.isAuthenticate(tokenId)) {
            return unAuthorizedResponse();
        }
        if (isAccessForbidden(tokenId)) {
            return forbiddenResponse();
        }
        GalleryItem galleryItem = galleryManager.getGalleryItemById(galleryItemDto.getId());
        if (galleryItem == null) {
            return notFoundResponse("Gallery item was not found!");
        }
        galleryItem.setTitle(galleryItemDto.getTitle());
        galleryManager.updateGalleryItem(galleryItem);
        return new ResponseEntity(HttpStatus.OK);
    }

    // TODO implement upload of file
    //    GalleryItemAdd = "/api/gallery/itemAdd",

    private boolean isAccessForbidden(String tokenId) {
        return !this.authentication.getAuthenticatedUser(tokenId).isUserInRole(Role.EDITOR);
    }
}
