package swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swiggy.wallet.entity.User;
import swiggy.wallet.service.UserService;

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
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reset(userService);
    }

    @Test
    void testRegisterNewUser_Successful() throws Exception {
        User userRequestModel = new User("testUser", "testPassword");
        User registeredUser = new User("testUser", "testPassword");

//        registeredUser.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));

        when(userService.register(userRequestModel)).thenReturn(registeredUser);

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isOk());

        verify(userService, times(1)).register(userRequestModel);
    }

    @Test
    void expectUserAlreadyExists() throws Exception {
        UserRequestModel userRequestModel = new UserRequestModel("testUser","testPassword");

        when(userService.register(userRequestModel)).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestModel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    void expectUserDeleted() throws Exception {
        when(userService.delete()).thenReturn(USER_DELETED_SUCCESSFULLY);

        mockMvc.perform(delete("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(USER_DELETED_SUCCESSFULLY));
        verify(userService, times(1)).delete();
    }

    @Test
    @WithMockUser(username = "userNotFound")
    void expectUserNotFoundException() throws Exception {
        String username = "userNotFound";
        String errorMessage = "User "+username+" not be found.";

        when(userService.delete()).thenThrow(new UserNotFoundException(errorMessage));

        mockMvc.perform(delete("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).delete();
    }

    @Test
    @WithMockUser(username = "sender")
    void expectTransactionSuccessful() throws Exception {
        TransactionRequestModel transactionRequestModel = new TransactionRequestModel("sender", new Money(100, Currency.INR));
        String requestJson = objectMapper.writeValueAsString(transactionRequestModel);
        when(userService.transact(transactionRequestModel)).thenReturn(TRANSACTION_SUCCESSFUL);

        mockMvc.perform(put("/api/v1/users/transact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(TRANSACTION_SUCCESSFUL));
    }
}