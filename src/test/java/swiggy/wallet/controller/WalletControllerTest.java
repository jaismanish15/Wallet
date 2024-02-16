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
    @WithMockUser(username = "mj", roles = "USER")
    public void testCreateWallet_AuthenticatedUser_ShouldReturnNewWallet() throws Exception {
        Wallet mockWallet = new Wallet(new Money(BigDecimal.ZERO, Currency.USD));
        when(walletService.create()).thenReturn(mockWallet);

        mockMvc.perform(post("/wallet/create")
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.money.amount").value(0.0));

        verify(walletService, times(1)).create();
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testCreateWallet_ShouldReturnNewWallet() throws Exception {
        Wallet mockWallet = new Wallet(new Money(BigDecimal.ZERO, Currency.USD));
        when(walletService.create()).thenReturn(mockWallet);

        mockMvc.perform(post("/wallet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.money.amount").value(0.0))
                .andExpect(jsonPath("$.money.currency").value("USD"));

        verify(walletService, times(1)).create();
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testGetWallet_ShouldReturnWalletBalance() throws Exception {
        Money mockBalance = new Money(new BigDecimal("100.00"), Currency.USD);
        when(walletService.getBalance(1L)).thenReturn(mockBalance);

        mockMvc.perform(get("/wallet/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(walletService, times(1)).getBalance(1L);
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testDeposit_ShouldReturnUpdatedBalance() throws Exception {
        Money depositMoney = new Money(new BigDecimal("50.00"), Currency.USD);
        Money updatedBalance = new Money(new BigDecimal("150.00"), Currency.USD);
        when(walletService.deposit(1L, depositMoney)).thenReturn(updatedBalance);

        mockMvc.perform(post("/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(depositMoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(walletService, times(1)).deposit(1L, depositMoney);
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testWithdraw_ShouldReturnUpdatedBalance() throws Exception {
        Money withdrawalMoney = new Money(new BigDecimal("20.00"), Currency.USD);
        Money updatedBalance = new Money(new BigDecimal("80.00"), Currency.USD);
        when(walletService.withdraw(1L, withdrawalMoney)).thenReturn(updatedBalance);

        mockMvc.perform(post("/wallet/1/withdraw")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalMoney)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(80.0))
                .andExpect(jsonPath("$.currency").value("USD"));

        verify(walletService, times(1)).withdraw(1L, withdrawalMoney);
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testCreateWalletAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testGetWalletAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testDepositAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/1/deposit")
                        .contentType("application/json")
                        .content("{\"amount\": 10, \"currency\": \"USD\"}")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @WithMockUser(username = "mj", roles = "USER")
    public void testWithdrawAuthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/1/withdraw")
                        .contentType("application/json")
                        .content("{\"amount\": 5, \"currency\": \"USD\"}")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateWalletUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/create"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testGetWalletUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/wallet/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testDepositUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/1/deposit")
                        .contentType("application/json")
                        .content("{\"amount\": 10, \"currency\": \"USD\"}")
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
    @WithMockUser(username = "mj", roles = "USER")
    public void testGetAllWallets() throws Exception {
        Wallet wallet = new Wallet(new Money(BigDecimal.valueOf(50), Currency.USD));

        when(walletService.fetchAll()).thenReturn(Collections.singletonList(wallet));

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/wallet/list"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(wallet.getId()))
                .andExpect(jsonPath("$[0].money.amount").value(wallet.getMoney().getAmount()));

        verify(walletService, times(1)).fetchAll();
        verifyNoMoreInteractions(walletService);
    }

}
