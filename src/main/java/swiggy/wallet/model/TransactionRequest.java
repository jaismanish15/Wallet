package swiggy.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.valueObject.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private Long senderWalletId;
    private Long receiverWalletId;
    private Long receiverId;
    private Money money;
}
