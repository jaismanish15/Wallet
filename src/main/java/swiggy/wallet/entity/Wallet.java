package swiggy.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Money money;

    public Wallet(){
        this.money = new Money();
    }
    public Wallet(Money money){
        this.money = new Money();
    }



    public Money deposit(Money depositMoney) {
        if (Objects.isNull(depositMoney) || depositMoney.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid deposit amount");
        }

        this.money = this.money.add(depositMoney);
        return this.money;
    }

    public Money withdraw(Money withdrawalMoney) throws InsufficientBalanceException {
        validateWithdrawalAmount(withdrawalMoney);

        BigDecimal remainingBalance = this.money.subtract(withdrawalMoney).getAmount();
        if (remainingBalance.doubleValue() < 0.00) {
            throw new InsufficientBalanceException("Insufficient Balance in Wallet");
        }

        this.money = this.money.subtract(withdrawalMoney);
        return this.money;
    }

    private void validateWithdrawalAmount(Money withdrawalMoney) {
        if (Objects.isNull(withdrawalMoney) || withdrawalMoney.getAmount().doubleValue() < 0.00) {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
    }


}
