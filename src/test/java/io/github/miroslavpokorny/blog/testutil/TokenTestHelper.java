package io.github.miroslavpokorny.blog.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.miroslavpokorny.blog.model.dto.LoggedUserDto;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TokenTestHelper {
    public static String getSignedAdministratorTokenId(MockMvc mvc) throws Exception {
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
        return loggedUser.getTokenId();
    }

    public static String getSignedUserTokenId(MockMvc mvc) throws Exception {
        MvcResult result = mvc.perform(post("/api/sign/in")
                .content("{\"email\":\"pepamizera@example.com\",\"password\":\"123\"}")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        ObjectMapper mapper = new ObjectMapper();
        String textJson = result.getResponse().getContentAsString();
        LoggedUserDto loggedUser = mapper.readValue(textJson, LoggedUserDto.class);
        Assert.assertEquals("pepamizera", loggedUser.getNickname());
        Assert.assertEquals(2, loggedUser.getId());
        Assert.assertEquals(1, loggedUser.getRole());
        Assert.assertTrue(loggedUser.getTokenId().length() > 0);
        return loggedUser.getTokenId();
    }
}
