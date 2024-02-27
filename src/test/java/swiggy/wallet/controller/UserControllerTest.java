package swiggy.wallet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Country;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.UserAlreadyPresentException;
import swiggy.wallet.model.UserResponse;
import swiggy.wallet.service.UserService;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        UserResponse expectedResponse = new UserResponse(1L, "testuser", null, Country.USA, "User Registered Successfully");
        User userToRegister = new User("testuser", "password", Country.USA);
        when(userService.register(any(User.class))).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(expectedResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedResponse.getMessage()));
    }

    @Test
    public void testRegisterUserAlreadyPresent() throws Exception {
        User userToRegister = new User("testuser", "password", Country.USA);
        when(userService.register(any(User.class)))
                .thenThrow(new UserAlreadyPresentException("User Already Registered"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User Already Registered"));
    }

    @Test
    @WithMockUser(username = "user")
    void testUserDeleted_Successful() throws Exception {
        when(userService.delete()).thenReturn("User Deleted Successfully");

        mockMvc.perform(delete("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("User Deleted Successfully"));
        verify(userService, times(1)).delete();
    }

    @Test
    @WithMockUser(username = "user")
    void testAddWalletAddedToUser() throws Exception {
        User user = new User("user", "pass", Country.INDIA);

        when(userService.addWallet()).thenReturn(user);

        mockMvc.perform(put("/api/users/addWallet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).addWallet();
    }


}