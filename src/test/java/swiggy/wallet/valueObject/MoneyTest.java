package swiggy.wallet.valueObject;

import org.junit.jupiter.api.Test;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {
    @Test
    public void testDepositSameCurrencySuccessfully() {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money depositMoney = new Money(new BigDecimal("50.00"), Currency.USD);

        Money result = initialMoney.add(depositMoney);

        assertEquals(new BigDecimal("150.00"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }


    @Test
    public void testDepositDifferentCurrency() {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money depositMoney = new Money(new BigDecimal("10.00"), Currency.EUR);

        Money result = initialMoney.add(depositMoney);

        assertEquals(new BigDecimal("111.20"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }
    @Test
    public void testDepositNegativeAmount() {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money depositMoney = new Money(new BigDecimal("-50.00"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> initialMoney.add(depositMoney));
    }

    @Test
    public void testWithdraw() throws InsufficientBalanceException {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money withdrawalMoney = new Money(new BigDecimal("50.00"), Currency.USD);

        Money result = initialMoney.subtract(withdrawalMoney);

        assertEquals(new BigDecimal("50.00"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }

    @Test
    public void testWithdrawNegativeAmount() {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money withdrawalMoney = new Money(new BigDecimal("-50.00"), Currency.USD);

        assertThrows(IllegalArgumentException.class, () -> initialMoney.subtract(withdrawalMoney));
    }

    @Test
    public void testWithdrawDifferentCurrency() throws InsufficientBalanceException {
        Money initialMoney = new Money(new BigDecimal("100.00"), Currency.USD);
        Money withdrawalMoney = new Money(new BigDecimal("10.00"), Currency.EUR);

        Money result = initialMoney.subtract(withdrawalMoney);

        assertEquals(new BigDecimal("88.80"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        Money initialMoney = new Money(new BigDecimal("50.00"), Currency.USD);
        Money withdrawalMoney = new Money(new BigDecimal("100.00"), Currency.USD);

        assertThrows(InsufficientBalanceException.class, () -> initialMoney.subtract(withdrawalMoney));
    }

    @Test
    public void testDepositDifferentCurrency_INR() {
        Money initialMoney = new Money(new BigDecimal("99.00"), Currency.USD);
        Money depositMoney = new Money(new BigDecimal("83.00"), Currency.INR);

        Money result = initialMoney.add(depositMoney);

        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }

    @Test
    public void testWithdrawDifferentCurrency_INR() throws InsufficientBalanceException {
        Money initialMoney = new Money(new BigDecimal("200.00"), Currency.USD);
        Money withdrawalMoney = new Money(new BigDecimal("83.00"), Currency.INR);

        Money result = initialMoney.subtract(withdrawalMoney);

        assertEquals(new BigDecimal("199.00"), result.getAmount());
        assertEquals(Currency.USD, result.getCurrency());
    }

}