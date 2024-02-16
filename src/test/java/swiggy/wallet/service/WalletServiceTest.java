package swiggy.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;


    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetWalletBalance() {
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));
        long id = mockWallet.getId();
        when(walletRepository.findById(id)).thenReturn(Optional.of(mockWallet));

        Money result = walletService.getBalance(id);

        assertEquals(new Money(BigDecimal.valueOf(100), Currency.USD), result);
        verify(walletRepository, times(1)).findById(id);
    }

    @Test
    void testDepositSameCurrency() {
        Money depositMoney = new Money(BigDecimal.valueOf(50), Currency.USD);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));
        long id = mockWallet.getId();
        when(walletRepository.findById(id)).thenReturn(Optional.of(mockWallet));

        Money result = walletService.deposit(id, depositMoney);

        assertEquals(new Money(new BigDecimal("150.00"), Currency.USD), result);
        verify(walletRepository, times(1)).save(mockWallet);
    }

    @Test
    void testDepositDifferentCurrency() {
        Money depositMoney = new Money(BigDecimal.valueOf(10), Currency.EUR);
        Wallet wallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(wallet));

        Money result = walletService.deposit(1L, depositMoney);

        assertEquals(new Money(BigDecimal.valueOf(110.75), Currency.USD), result);
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testWithdrawSameCurrency() {
        Money withdrawalMoney = new Money(BigDecimal.valueOf(50), Currency.USD);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));
        long id = mockWallet.getId();
        when(walletRepository.findById(id)).thenReturn(Optional.of(mockWallet));

        Money result = walletService.withdraw(id, withdrawalMoney);

        assertEquals(new Money(new BigDecimal("50.00"), Currency.USD), result);
        verify(walletRepository, times(1)).save(mockWallet);
    }

    @Test
    void testWithdrawDifferentCurrency() {
        Money withdrawalMoney = new Money(BigDecimal.valueOf(110), Currency.EUR);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));
        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(mockWallet));

        assertThrows(IllegalStateException.class, () -> walletService.withdraw(1L, withdrawalMoney));

        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawNegativeAmount() {
        Money withdrawalMoney = new Money(BigDecimal.valueOf(-10), Currency.USD);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(mockWallet));

        assertThrows(IllegalArgumentException.class, () -> walletService.withdraw(1L, withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        Money withdrawalMoney = new Money(BigDecimal.valueOf(150), Currency.USD);
        Wallet mockWallet = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));

        when(walletRepository.findById(anyLong())).thenReturn(Optional.of(mockWallet));

        assertThrows(IllegalStateException.class, () -> walletService.withdraw(1L, withdrawalMoney));
        verify(walletRepository, never()).save(any());
    }

    @Test
    void testGetAllWallets() {
        Wallet wallet1 = new Wallet(new Money(BigDecimal.valueOf(50), Currency.USD));
        Wallet wallet2 = new Wallet(new Money(BigDecimal.valueOf(100), Currency.USD));

        when(walletRepository.findAll()).thenReturn(List.of(wallet1, wallet2));

        List<Wallet> result = walletService.fetchAll();

        assertEquals(2, result.size());
        assertEquals(wallet1, result.get(0));
        assertEquals(wallet2, result.get(1));

        verify(walletRepository, times(1)).findAll();
        verifyNoMoreInteractions(walletRepository);
    }


}
