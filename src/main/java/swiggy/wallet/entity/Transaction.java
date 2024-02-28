package swiggy.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.valueObject.Money;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transactionId;

    private LocalDateTime timestamp;

    private Money money;

    @ManyToOne(cascade = CascadeType.ALL)
    private User sender;

    private Long senderWalletId;

    @ManyToOne(cascade = CascadeType.ALL)
    private User receiver;

    private Long receiverWalletId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "service_charge")),
            @AttributeOverride(name = "currency", column = @Column(name = "service_charge_currency"))
    })
    private Money serviceCharge;

    public Transaction(Money money, Money serviceCharge, User sender, Long senderWalletId, User receiver, Long receiverWalletId,LocalDateTime timestamp) {
        this.money = money;
        this.serviceCharge = serviceCharge;
        this.sender = sender;
        this.receiver = receiver;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.timestamp = timestamp;
    }
}
