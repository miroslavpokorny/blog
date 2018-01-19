package io.github.miroslavpokorny.blog;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.miroslavpokorny.blog.model.dto.LoggedUserDto;
import io.github.miroslavpokorny.blog.model.manager.DefaultUserManager;
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
    public void getAdministratorUser() throws Exception {
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
}
