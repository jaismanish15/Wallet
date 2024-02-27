package swiggy.wallet.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import swiggy.wallet.valueObject.Money;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("transactionResponse")
public class TransactionResponse {
    private String message;
    private LocalDateTime timeStamp = LocalDateTime.now();
    private Long senderWalletId;
    private Long receiverWalletId;
    private Money money;
    private Money serviceFee;


    public TransactionResponse(String message) {
        this.message = message;
    }
}
