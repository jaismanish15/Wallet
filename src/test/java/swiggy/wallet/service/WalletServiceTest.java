package swiggy.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Country;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.AuthenticationFailed;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
    }

    @Test
    void testDepositSuccessful() throws UserNotFoundException, AuthenticationFailed {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(Country.USA);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        Money result = walletService.deposit(1L, "user", depositMoney);

        assertEquals(new Money(new BigDecimal("50.00"), Currency.USD), result);
        verify(walletRepository, times(1)).save(any());

    }

    @Test
    void testWithdrawSuccessful() throws UserNotFoundException, InsufficientBalanceException, AuthenticationFailed {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(Country.USA);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        walletService.deposit(1L, "user", new Money(BigDecimal.valueOf(150), Currency.USD));

        Money withdrawMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        Money result = walletService.withdraw(1L, "user", withdrawMoney);

        assertEquals(new Money(new BigDecimal("100.00"), Currency.USD), result);
        verify(walletRepository, times(2)).save(any());
    }

    @Test
    void testDepositWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(AuthenticationFailed.class, () -> walletService.deposit(2L, "xyz", depositMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money withdrawalMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(AuthenticationFailed.class, () -> walletService.withdraw(2L, "xyz", withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInsufficientFunds() {
        User user = new User("user", "password", Country.INDIA);
        Wallet wallet = new Wallet(Country.USA);

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(walletRepository.findById(1L)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money withdrawMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(1L, "user", withdrawMoney));
    }

}
