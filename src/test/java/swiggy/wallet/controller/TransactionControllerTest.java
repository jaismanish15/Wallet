package swiggy.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.service.TransactionService;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();    }

    @Test
    void testTransact_SuccessfulTransaction_Returns200() throws Exception {
        TransactionRequest request = new TransactionRequest(2L, new Money(new BigDecimal("100.00"), Currency.USD));
        when(transactionService.transact(any(TransactionRequest.class))).thenReturn("Transaction Successful");

        ResultActions resultActions = mockMvc.perform(post("/transactions/transact/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isOk());
        verify(transactionService, times(1)).transact(any(TransactionRequest.class));
    }

    @Test
    void testTransact_InsufficientBalance_ReturnsBadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest(2L, new Money(new BigDecimal("1000.00"), Currency.USD));
        when(transactionService.transact(request)).thenThrow(new InsufficientBalanceException("Insufficient Balance"));

        ResultActions resultActions = mockMvc.perform(post("/transactions/transact/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient Balance"));

        verify(transactionService, times(1)).transact(any(TransactionRequest.class));
    }

}
