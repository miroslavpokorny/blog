package io.github.miroslavpokorny.blog.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.miroslavpokorny.blog.model.dto.CategoryDto;
import io.github.miroslavpokorny.blog.model.dto.CategoryListDto;
import io.github.miroslavpokorny.blog.testutil.TokenTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(secure = false)
@ActiveProfiles("TEST")
public class CategoryControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void getCategoryList() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        MvcResult result = mvc.perform(post("/api/category/list")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk()).andReturn();
        String textJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CategoryListDto categoryList = mapper.readValue(textJson, CategoryListDto.class);
        Assert.assertFalse(categoryList.getCategories().isEmpty());
        CategoryDto categoryOne = categoryList.getCategories().stream().filter(categoryDto -> categoryDto.getId() == 1).findFirst().orElse(null);
        CategoryDto categoryTwo = categoryList.getCategories().stream().filter(categoryDto -> categoryDto.getId() == 2).findFirst().orElse(null);
        CategoryDto categoryThree = categoryList.getCategories().stream().filter(categoryDto -> categoryDto.getId() == 3).findFirst().orElse(null);
        Assert.assertNotNull(categoryOne);
        Assert.assertNotNull(categoryTwo);
        Assert.assertNotNull(categoryThree);
        Assert.assertTrue(categoryOne.getName().equals("Category one"));
        Assert.assertTrue(categoryTwo.getName().equals("Category two"));
        Assert.assertTrue(categoryThree.getName().equals("Category three"));
        Assert.assertNotNull(categoryOne.getDescription());
        Assert.assertNotNull(categoryTwo.getDescription());
        Assert.assertNull(categoryThree.getDescription());
    }

    @Test
    public void getCategoryListUnauthorized() throws Exception {
        mvc.perform(post("/api/category/list")
                .param("tokenId", "not existing token ID")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void getCategoryListBadRequest() throws Exception {
        mvc.perform(post("/api/category/list")).andExpect(status().isBadRequest());
    }

    @Test
    public void getCategoryListForbidden() throws Exception {
        String tokenId = TokenTestHelper.getSignedUserTokenId(mvc);
        mvc.perform(post("/api/category/list")
                .param("tokenId", tokenId)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void createCategory() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        String jsonData = "{\"name\":\"abc1\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/add")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isOk());
    }

    @Test
    public void createCategoryUnauthorized() throws Exception {
        String jsonData = "{\"name\":\"abc2\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/add")
                .param("tokenId", "unauthorized token ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void createCategoryForbidden() throws Exception {
        String tokenId = TokenTestHelper.getSignedUserTokenId(mvc);
        String jsonData = "{\"name\":\"abc3\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/add")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void createCategoryConflict() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        String jsonData = "{\"name\":\"Category one\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/add")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isConflict());
    }

    @Test
    public void removeCategory() throws Exception {
        String jsonData = "{\"id\":4}";
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        mvc.perform(post("/api/category/remove")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isOk());
        MvcResult result = mvc.perform(post("/api/category/list")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk()).andReturn();
        String textJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CategoryListDto categoryList = mapper.readValue(textJson, CategoryListDto.class);
        Assert.assertNull(categoryList.getCategories().stream().filter(categoryDto -> categoryDto.getId() == 4).findFirst().orElse(null));
    }

    @Test
    public void removeCategoryUnauthorized() throws Exception {
        String jsonData = "{\"id\":5}";
        mvc.perform(post("/api/category/remove")
                .param("tokenId", "Not existing token ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void removeCategoryForbidden() throws Exception {
        String jsonData = "{\"id\":6}";
        String tokenId = TokenTestHelper.getSignedUserTokenId(mvc);
        mvc.perform(post("/api/category/remove")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isForbidden());
    }

    @Test
    public void editCategory() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        String jsonData = "{\"id\": 7, \"name\":\"ToEdit1-Edited\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/edit")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isOk());
        MvcResult result = mvc.perform(post("/api/category/list")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk()).andReturn();
        String textJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CategoryListDto categoryList = mapper.readValue(textJson, CategoryListDto.class);
        CategoryDto category = categoryList.getCategories().stream().filter(categoryDto -> categoryDto.getId() == 7).findFirst().orElse(null);
        Assert.assertNotNull(category);
        Assert.assertTrue(category.getName().equals("ToEdit1-Edited"));
        Assert.assertTrue("description".equals(category.getDescription()));
    }

    @Test
    public void editCategoryNotFound() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        String jsonData = "{\"id\": -1, \"name\":\"Not exist\", \"description\":\"Not exist\"}";
        mvc.perform(post("/api/category/edit")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void editCategoryUnauthorized() throws Exception {
        String jsonData = "{\"id\": 8, \"name\":\"ToEdit2-Edited\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/edit")
                .param("tokenId", "Unauthorized token ID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void editCategoryForbidden() throws Exception {
        String tokenId = TokenTestHelper.getSignedUserTokenId(mvc);
        String jsonData = "{\"id\": 9, \"name\":\"ToEdit3-Edited\", \"description\":\"description\"}";
        mvc.perform(post("/api/category/edit")
                .param("tokenId", tokenId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
        ).andExpect(status().isForbidden());
    }
}
