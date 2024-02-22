package swiggy.wallet.service;

import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.valueObject.Money;

public interface TransactionService {
    public String transact(TransactionRequest request) throws UserNotFoundException, InsufficientBalanceException;
}
