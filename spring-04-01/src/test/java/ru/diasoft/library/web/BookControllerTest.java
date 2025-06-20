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
import ru.diasoft.library.service.BookService;
import ru.diasoft.library.web.security.AuthTokenFilter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private AuthTokenFilter authTokenFilter;
    @MockitoBean
    private BookService bookService;

    @Test
    void createBook_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/book")
                .with(csrf().asHeader()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBooks_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBook_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateBook_unauthorized() throws Exception {
        String json = "{\"id\": 1,\"title\": \"Убийство в восточном экспрессе\", \"author\": \"Агата Кристи\", \"genre\": \"Детектив\"}";
        mvc.perform(MockMvcRequestBuilders.put("/book/1")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBook_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/book/1")
                        .with(csrf().asHeader()))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "admin")
    @Test
    void createBook_authorized() throws Exception {
        String json = "{\"id\": 1,\"title\": \"Убийство в восточном экспрессе\", \"author\": \"Агата Кристи\", \"genre\": \"Детектив\"}";
        mvc.perform(MockMvcRequestBuilders.post("/book")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void findBooks_authorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void findBook_authorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void updateBook_authorized() throws Exception {
        String json = "{\"id\": 1,\"title\": \"Убийство в восточном экспрессе\", \"author\": \"Агата Кристи\", \"genre\": \"Детектив\"}";
        mvc.perform(MockMvcRequestBuilders.put("/book/1")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin")
    @Test
    void deleteBook_authorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/book/1")
                        .with(csrf().asHeader()))
                .andExpect(status().is2xxSuccessful());
    }
}
