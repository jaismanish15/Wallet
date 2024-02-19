package swiggy.wallet.service;

import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.AuthenticationFailed;
import swiggy.wallet.valueObject.Money;

import java.util.List;

public interface WalletService {

    Money deposit(String username, Money depositMoney) throws AuthenticationFailed;

    Money withdraw(String username, Money withdrawalMoney) throws AuthenticationFailed;




}
