package swiggy.wallet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WalletTest {

    @Mock
    private Money mockMoney;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWalletInitialization() {
        Wallet wallet = new Wallet();

        assertEquals(0, wallet.getId());
        assertEquals(0.00, wallet.getMoney().getAmount().doubleValue());
    }

    @Test
    void testDeposit_ValidDeposit_ShouldIncreaseBalance() {
        Wallet wallet = new Wallet();
        Money depositMoney = new Money(new BigDecimal("5.00"), Currency.USD);
        Money newBalance1 = wallet.deposit(depositMoney);
        assertEquals(new BigDecimal("5.00"), newBalance1.getAmount());
        Money newBalance2 = wallet.deposit(depositMoney);
        assertEquals(new BigDecimal("10.00"), newBalance2.getAmount());

    }

    @Test
    void testDeposit_InvalidDepositAmount_ShouldThrowException() {
        Wallet wallet = new Wallet();
        Money invalidDeposit = new Money(new BigDecimal("-10"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> wallet.deposit(invalidDeposit));
    }

    @Test
    void testWithdraw_ValidWithdrawal_ShouldDecreaseBalance() throws InsufficientBalanceException {
        Wallet wallet = new Wallet();
        Money depositMoney = new Money(new BigDecimal("15.00"), Currency.USD);
        wallet.deposit(depositMoney);
        Money withdrawalMoney = new Money(new BigDecimal("7.50"), Currency.USD);

        Money newBalance = wallet.withdraw(withdrawalMoney);

        assertEquals(new BigDecimal("7.50"), newBalance.getAmount());
    }

    @Test
    void testWithdraw_InvalidWithdrawalAmount_ShouldThrowException() {
        Wallet wallet = new Wallet();
        Money invalidWithdrawal = new Money(new BigDecimal("-5.00"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> wallet.withdraw(invalidWithdrawal));
    }

    @Test
    void testWithdraw_InsufficientFunds_ShouldThrowException() {
        Wallet wallet = new Wallet();
        Money withdrawalMoney = new Money(new BigDecimal("25.00"), Currency.USD);

        assertThrows(IllegalStateException.class, () -> wallet.withdraw(withdrawalMoney));
    }
}
