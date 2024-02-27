package swiggy.wallet;

import swiggy.wallet.enums.Currency;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;

public class Constant {
    public static Money SERVICE_FEE = new Money(new BigDecimal("10.00"), Currency.INR);
}
