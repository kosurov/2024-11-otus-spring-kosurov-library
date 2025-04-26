package ru.diasoft.library.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.diasoft.library.config.SpringSecurityConfig;
import ru.diasoft.library.service.CommentService;
import ru.diasoft.library.web.security.JwtUtils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@Import(SpringSecurityConfig.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private UserDetailsService userService;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void createComment_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/comment")
                        .with(csrf().asHeader()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBookComments_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment")
                        .param("bookId", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteComment_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "admin")
    @Test
    void createComment_authorized() throws Exception {
        String json = "{\"bookId\": 1,\"text\": \"Норм\"}";
        mvc.perform(MockMvcRequestBuilders.post("/comment")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void findBookComments_authorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/comment")
                        .param("bookId", "1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void deleteComment_authorized_hasNoRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"MODERATOR"})
    @Test
    void deleteComment_authorized_hasRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/1")
                        .with(csrf().asHeader()))
                .andExpect(status().is2xxSuccessful());
    }
}
