package swiggy.wallet.service;

import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.AuthenticationFailed;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.valueObject.Money;

import java.util.List;

public interface WalletService {

    Money deposit(Long walletId, Money depositMoney) throws AuthenticationFailed, UserNotFoundException;

    Money withdraw(Long walletId, Money withdrawalMoney) throws AuthenticationFailed, InsufficientBalanceException, UserNotFoundException;




}
