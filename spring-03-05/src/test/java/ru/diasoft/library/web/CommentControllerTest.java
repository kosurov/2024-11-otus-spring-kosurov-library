package ru.diasoft.library.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.diasoft.library.service.CommentService;
import ru.diasoft.library.web.security.AuthTokenFilter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private AuthTokenFilter authTokenFilter;
    @MockitoBean
    private CommentService commentService;

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
    void deleteComment_authorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/1")
                        .with(csrf().asHeader()))
                .andExpect(status().is2xxSuccessful());
    }
}
