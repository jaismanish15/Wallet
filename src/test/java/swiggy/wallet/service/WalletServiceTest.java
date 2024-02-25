package swiggy.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;
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
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
    }

    @Test
    void testDepositSuccessful() throws  UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money result = walletService.deposit(1L, depositMoney);

        assertEquals(new Money(new BigDecimal("50.00"), Currency.USD), result);
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void testWithdrawSuccessful() throws UserNotFoundException, InsufficientBalanceException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        walletService.deposit( 1L, new Money(BigDecimal.valueOf(50), Currency.USD));
        Money withdrawalMoney = new Money(new BigDecimal("50.00"), Currency.USD);
        Money expectedBalance = new Money(new BigDecimal("0.00"), Currency.USD);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Money result = walletService.withdraw(1L, withdrawalMoney);

        assertEquals(expectedBalance, result);
        verify(walletRepository, times(2)).save(any());
    }

    @Test
    void testDepositWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(UserNotFoundException.class, () -> walletService.deposit(2L, depositMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInvalidUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Money withdrawalMoney = new Money(BigDecimal.valueOf(50), Currency.USD);

        assertThrows(UserNotFoundException.class, () -> walletService.withdraw(2L, withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawWithInsufficientFunds() throws InsufficientBalanceException {
        Wallet mockWallet = mock(Wallet.class);

        Money withdrawalMoney = new Money(BigDecimal.valueOf(150), Currency.USD);
        doThrow(InsufficientBalanceException.class).when(mockWallet).withdraw(withdrawalMoney);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        mockUser.setWallet(mockWallet);

        when(walletRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        assertThrows(InsufficientBalanceException.class, () -> walletService.withdraw(1L, withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

}
