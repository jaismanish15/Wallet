package swiggy.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;


@Table(name = "users")
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_id")
    @Builder.Default
    private Wallet wallet = new Wallet();


    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

}
