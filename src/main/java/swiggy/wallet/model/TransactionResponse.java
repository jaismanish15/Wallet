package swiggy.wallet.model;

import java.time.LocalDateTime;

public class TransactionResponse {
    private String message;
    private LocalDateTime timeStamp = LocalDateTime.now();

    public TransactionResponse(String message) {
        this.message = message;
    }
}
