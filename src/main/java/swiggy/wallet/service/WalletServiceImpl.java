package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.exception.AuthenticationFailed;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.repository.WalletRepository;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private UserRepository userRepository;





    @Override
    public Money deposit(String username, Money depositMoney) throws AuthenticationFailed {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AuthenticationFailed("Username or password does not match."));
        Wallet wallet = user.getWallet();
        Money updatedBalance = wallet.deposit(depositMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }

    @Override
    public Money withdraw(String username, Money withdrawalMoney) throws AuthenticationFailed {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AuthenticationFailed("Username or password does not match."));
        Wallet wallet = user.getWallet();
        Money updatedBalance = wallet.withdraw(withdrawalMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }


}