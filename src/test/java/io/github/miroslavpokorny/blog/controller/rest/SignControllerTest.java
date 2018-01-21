package io.github.miroslavpokorny.blog.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.miroslavpokorny.blog.model.dto.ErrorMessageDto;
import io.github.miroslavpokorny.blog.model.dto.LoggedUserDto;
import io.github.miroslavpokorny.blog.testutil.TokenTestHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class SignControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void badPasswordSignIn() throws Exception {
        mvc.perform(post("/api/sign/in")
                .content("{\"email\":\"frantanovak@example.com\",\"password\":\"badpass\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void missingPropertyDataSignIn() throws Exception {
        mvc.perform(post("/api/sign/in")
                .content("{\"email\":\"frantanovak@example.com\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        mvc.perform(post("/api/sign/in")
                .content("\"password\":\"badpass\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
        mvc.perform(post("/api/sign/in")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void signIn() throws Exception {
        MvcResult result = mvc.perform(post("/api/sign/in")
                .content("{\"email\":\"frantanovak@example.com\",\"password\":\"abc\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        String textJson = result.getResponse().getContentAsString();
        LoggedUserDto loggedUser = mapper.readValue(textJson, LoggedUserDto.class);
        Assert.assertEquals("frantanovak", loggedUser.getNickname());
        Assert.assertEquals(1, loggedUser.getId());
        Assert.assertEquals(4, loggedUser.getRole());
        Assert.assertTrue(loggedUser.getTokenId().length() > 0);
    }



    @Test
    public void signOutUserTest() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        mvc.perform(post("/api/sign/out")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk());
    }

    @Test
    public void signOutUnauthorizedUser() throws Exception {
        String tokenId = "";
        mvc.perform(post("/api/sign/out")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk());
        mvc.perform(post("/api/sign/out")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void getLoggedUser() throws Exception {
        String tokenId = TokenTestHelper.getSignedAdministratorTokenId(mvc);
        MvcResult result = mvc.perform(post("/api/getLoggedUser")
                .param("tokenId", tokenId)
        ).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        String textJson = result.getResponse().getContentAsString();
        LoggedUserDto loggedUser = mapper.readValue(textJson, LoggedUserDto.class);
        Assert.assertEquals("frantanovak", loggedUser.getNickname());
        Assert.assertEquals(1, loggedUser.getId());
        Assert.assertEquals(4, loggedUser.getRole());
    }

    @Test
    public void getLoggedUserUnauthorized() throws Exception {
        String tokenId = "not existing token";
        mvc.perform(post("/api/getLoggedUser")
                .param("tokenId", tokenId)
        ).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/getLoggedUser")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void signUpTest() throws Exception {
        String signUp = "{\"email\":\"test@test.test\",\"nickname\":\"test\",\"password\":\"password\",\"name\":\"test\",\"surname\":\"test\"}";
        MvcResult result = mvc.perform(post("/api/sign/up")
                .content(signUp)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        String textJson = result.getResponse().getContentAsString();
        LoggedUserDto loggedUser = mapper.readValue(textJson, LoggedUserDto.class);
        Assert.assertEquals("test", loggedUser.getNickname());
        Assert.assertEquals(1, loggedUser.getRole());
    }

    @Test
    public void signUpTestInvalidData() throws Exception {
        String signUp = "{\"email\":\"test\",\"nickname\":\"test\",\"password\":\"password\",\"name\":\"test\",\"surname\":\"test\"}";
        MvcResult result = mvc.perform(post("/api/sign/up")
                .content(signUp)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        String textJson = result.getResponse().getContentAsString();
        ErrorMessageDto error = mapper.readValue(textJson, ErrorMessageDto.class);
        Assert.assertTrue(error.getMessage().toLowerCase().contains("bad data format"));
        Assert.assertEquals("ErrorMessageDto", error.getType());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), error.getCode());
    }

    @Test
    public void signUpConflictData() throws Exception {
        String signUpEmailConflict = "{\"email\":\"frantanovak@example.com\",\"nickname\":\"test\",\"password\":\"password\",\"name\":\"test\",\"surname\":\"test\"}";
        mvc.perform(post("/api/sign/up")
                .content(signUpEmailConflict)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
        String signUpNicknameConflict = "{\"email\":\"some@example.com\",\"nickname\":\"frantanovak\",\"password\":\"password\",\"name\":\"test\",\"surname\":\"test\"}";
        mvc.perform(post("/api/sign/up")
                .content(signUpNicknameConflict)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }
}
