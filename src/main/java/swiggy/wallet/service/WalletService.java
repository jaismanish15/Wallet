package swiggy.wallet.service;

import swiggy.wallet.entity.Wallet;
import swiggy.wallet.valueObject.Money;

import java.util.List;

public interface WalletService {
    Wallet create();

    Money getBalance(Long walletId);

    Money deposit(Long walletId, Money depositMoney);

    Money withdraw(Long walletId, Money withdrawalMoney);

    List<Wallet> fetchAll();


}
