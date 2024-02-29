package swiggy.wallet.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.valueObject.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("walletResponse")
public class WalletResponse {
    private Long walletId;
    private Money money;
    private String message;
}
