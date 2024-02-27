package swiggy.wallet.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.enums.Country;
import swiggy.wallet.valueObject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Country country;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Wallet> wallets = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    public User(String username, String password, Country country){
        this.username = username;
        this.password = password;
        this.country = country;
        this.wallets.add(new Wallet(country));
    }
}
