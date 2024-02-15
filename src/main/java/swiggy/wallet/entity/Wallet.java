package swiggy.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Money money;


    public Wallet(Money money) {
        this.money = money;
    }

    public Money deposit(Money depositMoney) {
        if (Objects.isNull(depositMoney) || depositMoney.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid deposit amount");
        }

        this.money = this.money.add(depositMoney);
        return this.money;
    }

    public Money withdraw(Money withdrawalMoney) {
        if (Objects.isNull(withdrawalMoney) || withdrawalMoney.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }

        this.money = this.money.subtract(withdrawalMoney);
        return this.money;
    }

}
