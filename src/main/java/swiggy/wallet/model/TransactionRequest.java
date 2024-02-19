package swiggy.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.valueObject.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String receiverName;
    private Money money;
}
