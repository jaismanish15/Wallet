package swiggy.wallet.valueObject;

import jakarta.persistence.Embeddable;
import lombok.Data;
import swiggy.wallet.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


@Data
@Embeddable
public class Money {

    private BigDecimal amount;
    private Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    public Money(){
        this.amount = new BigDecimal("0.0");
        this.currency = Currency.USD;
    }

    public Money add(Money depositMoney) {
        if (depositMoney.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Deposit amount must be non-negative");
        }

        Money convertedDeposit = convertCurrency(depositMoney);
        return new Money(amount.add(convertedDeposit.getAmount()), currency);
    }

    public Money subtract(Money withdrawalMoney) {
        if (withdrawalMoney.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Withdrawal amount must be non-negative");
        }

        Money convertedWithdrawal = convertCurrency(withdrawalMoney);
        if (amount.compareTo(convertedWithdrawal.getAmount()) < 0) {
            throw new IllegalStateException("Insufficient funds for withdrawal");
        }

        return new Money(amount.subtract(convertedWithdrawal.getAmount()), currency);
    }

    private Money convertCurrency(Money money) {
        BigDecimal exchangeRate = money.getCurrency().getExchangeRate();
        BigDecimal convertedAmount = money.getAmount().divide(exchangeRate, 2, RoundingMode.HALF_UP);
        return new Money(convertedAmount, this.currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) &&
                currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
