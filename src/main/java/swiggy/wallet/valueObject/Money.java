package swiggy.wallet.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Objects;


@Data
@Embeddable
public class Money implements Comparable<Money> {

    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    public Money(Currency currency) {
        this.amount = new BigDecimal("0.0");
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
        return new Money(amount.add(convertedDeposit.getAmount()), this.currency);
    }

    public Money subtract(Money withdrawalMoney) throws InsufficientBalanceException {
        if (withdrawalMoney.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Withdrawal amount must be non-negative");
        }

        Money convertedWithdrawal = convertCurrency(withdrawalMoney);
        if (amount.compareTo(convertedWithdrawal.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient funds for withdrawal");
        }

        return new Money(amount.subtract(convertedWithdrawal.getAmount()), this.currency);
    }

    private Money convertCurrency(Money money) {
        return new Money(money.getCurrency().convertAmount(money.getAmount(), this.currency), this.currency);
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

    @Override
    public int compareTo(Money otherMoney) {
        Money convertedOtherMoney = convertCurrency(otherMoney);
        return this.amount.compareTo(convertedOtherMoney.getAmount());
    }
}
