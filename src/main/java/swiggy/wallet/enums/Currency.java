package swiggy.wallet.enums;

import java.math.BigDecimal;

public enum Currency {
    USD(BigDecimal.ONE),
    INR(new BigDecimal("83.0")),
    EUR(new BigDecimal("0.93"));

    private final BigDecimal exchangeRate;

    Currency(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
}
