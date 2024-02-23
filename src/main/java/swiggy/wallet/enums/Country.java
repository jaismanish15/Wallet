package swiggy.wallet.enums;

import lombok.Data;
import lombok.Getter;

import static swiggy.wallet.enums.Currency.INR;
import static swiggy.wallet.enums.Currency.USD;


@Getter
public enum Country {
    INDIA(INR),
    USA(USD);
    private final Currency currency;
    Country(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }
}
