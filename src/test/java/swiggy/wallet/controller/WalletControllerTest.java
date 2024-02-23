package swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.service.WalletService;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletController.class)
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDepositUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/1/deposit")
                        .contentType("application/json")
                        .content("{\"amount\": 5, \"currency\": \"USD\"}")
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testWithdrawUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/1/withdraw")
                        .contentType("application/json")
                        .content("{\"amount\": 5, \"currency\": \"USD\"}")
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "mj")
    public void testDepositAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 10, \"currency\": \"USD\"}")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "mj")
    public void testDeposit_Successful() throws Exception {
        Money depositMoney = new Money(new BigDecimal("50.00"), Currency.USD);
        Money updatedBalance = new Money(new BigDecimal("50.00"), Currency.USD);
        when(walletService.deposit(1L, depositMoney)).thenReturn(updatedBalance);

        mockMvc.perform(post("/api/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(depositMoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(50.0))
                .andExpect(jsonPath("$.currency").value("USD"));
        verify(walletService, times(1)).deposit(1L, depositMoney);
    }

    @Test
    @WithMockUser(username = "mj")
    public void testWithdraw_ShouldReturnUpdatedBalance() throws Exception {
        walletService.deposit(1L, new Money(new BigDecimal("50.00"), Currency.USD));
        Money withdrawalMoney = new Money(new BigDecimal("20.00"), Currency.USD);
        Money updatedBalance = new Money(new BigDecimal("30.00"), Currency.USD);
        when(walletService.withdraw(1L,withdrawalMoney)).thenReturn(updatedBalance);

        mockMvc.perform(post("/api/wallet/1/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalMoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(30.0))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(walletService, times(1)).withdraw(1L, withdrawalMoney);
    }

    @Test
    @WithMockUser(username = "mj")
    public void testWithdrawAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/1/withdraw")
                        .contentType("application/json")
                        .content("{\"amount\": 5, \"currency\": \"USD\"}")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
