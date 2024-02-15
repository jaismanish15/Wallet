package swiggy.wallet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.repository.WalletRepository;
import swiggy.wallet.service.WalletService;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WalletTest {


    @Test
    public void testWalletInitialization() {
        Money initialBalance = new Money(BigDecimal.valueOf(100), Currency.USD);
        Wallet wallet1 = new Wallet(initialBalance);

        assertEquals(0, wallet1.getId());
        assertEquals(initialBalance, wallet1.getMoney());
    }

    @Test
    void testDeposit_ValidDeposit_ShouldIncreaseBalance() {
        Wallet wallet = new Wallet(new Money(new BigDecimal("10.00"), Currency.USD));
        Money depositMoney = new Money(new BigDecimal("5.00"), Currency.USD);

        Money newBalance = wallet.deposit(depositMoney);

        assertEquals(new BigDecimal("15.00"), newBalance.getAmount());
    }

    @Test
    void testDeposit_InvalidDepositAmount_ShouldThrowException() {
        Wallet wallet = new Wallet(new Money(new BigDecimal("10.00"), Currency.USD));
        Money invalidDeposit = new Money(new BigDecimal("-10"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> wallet.deposit(invalidDeposit));
    }

    @Test
    void testWithdraw_ValidWithdrawal_ShouldDecreaseBalance() {
        Wallet wallet = new Wallet(new Money(new BigDecimal("20.00"), Currency.USD));
        Money withdrawalMoney = new Money(new BigDecimal("7.50"), Currency.USD);

        Money newBalance = wallet.withdraw(withdrawalMoney);

        assertEquals(new BigDecimal("12.50"), newBalance.getAmount());
    }

    @Test
    void testWithdraw_InvalidWithdrawalAmount_ShouldThrowException() {
        Wallet wallet = new Wallet(new Money(new BigDecimal("20.00"), Currency.USD));
        Money invalidWithdrawal = new Money(new BigDecimal("-5.00"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> wallet.withdraw(invalidWithdrawal));
    }

    @Test
    void testWithdraw_InsufficientFunds_ShouldThrowException() {
        Wallet wallet = new Wallet(new Money(new BigDecimal("20.00"), Currency.USD));
        Money withdrawalMoney = new Money(new BigDecimal("25.00"), Currency.USD);

        assertThrows(IllegalStateException.class, () -> wallet.withdraw(withdrawalMoney));
    }

}
