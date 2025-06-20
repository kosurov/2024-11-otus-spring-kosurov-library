package ru.diasoft.library.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.diasoft.library.config.SpringSecurityConfig;
import ru.diasoft.library.service.UserService;
import ru.diasoft.library.web.security.JwtUtils;
import ru.diasoft.library.web.security.LibraryUserDetails;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SpringSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void login_unauthorized() throws Exception {
        String json = "{\"username\": \"admin\",\"password\": \"admin\"}";

        Mockito.when(userService.loadUserByUsername("admin")).thenReturn(new LibraryUserDetails("admin",
                "$2a$12$aR4AaQFnHrJEMx/YN22f5uaauMjvmVNN2ni2gkK.CxoliMVRRBJUK", Set.of()));

        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void getUsers_unauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "admin")
    @Test
    void getUsers_authenticated_hasNoRoles() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void getUsers_authorized_hasRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk());
    }
}
