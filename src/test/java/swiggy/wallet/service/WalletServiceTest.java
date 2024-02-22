package swiggy.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.AuthenticationFailed;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;
import swiggy.wallet.service.WalletService;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockUser = new User();
    }

    @Test
    void testDepositSuccessful() throws AuthenticationFailed {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money result = walletService.deposit("testUser", depositMoney);

        assertEquals(new Money(new BigDecimal("50.00"), Currency.USD), result);
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void testWithdrawSuccessful() throws AuthenticationFailed, InsufficientBalanceException {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        walletService.deposit( "testUser", new Money(BigDecimal.valueOf(50), Currency.USD));
        Money withdrawalMoney = new Money(new BigDecimal("50.00"), Currency.USD);
        Money expectedBalance = new Money(new BigDecimal("0.00"), Currency.USD);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money result = walletService.withdraw("testUser", withdrawalMoney);

        assertEquals(expectedBalance, result);
        verify(walletRepository, times(2)).save(any());
    }

    @Test
    void testDepositWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(AuthenticationFailed.class, () -> walletService.deposit("nonexistentUser", depositMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money withdrawalMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(AuthenticationFailed.class, () -> walletService.withdraw("nonexistentUser", withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInsufficientFunds() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        Money withdrawalMoney = new Money(BigDecimal.valueOf(150), Currency.USD);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));
        mockUser.setWallet(mockWallet);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        assertThrows(IllegalStateException.class, () -> walletService.withdraw("testUser", withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }


}
