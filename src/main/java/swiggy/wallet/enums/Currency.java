package swiggy.wallet.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
public enum Currency {
    USD(new BigDecimal("83.0")),
    INR(BigDecimal.ONE),
    EUR(new BigDecimal("93.0"));

    private final BigDecimal exchangeRate;

    Currency(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal convertAmount(BigDecimal amount, Currency targetCurrency) {
        if (this == targetCurrency) {
            return amount;
        }

        BigDecimal sourceExchangeRate = this.getExchangeRate();
        BigDecimal targetExchangeRate = targetCurrency.getExchangeRate();

        BigDecimal inrAmount = amount.multiply(sourceExchangeRate);
        return inrAmount.divide(targetExchangeRate,2, RoundingMode.HALF_UP);
    }
}
