package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.exception.*;
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
    public Money deposit(Long walletId, String username, Money depositMoney) throws UserNotFoundException, AuthenticationFailed {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AuthenticationFailed("Username or password does not match."));
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet Not Found"));
        Money updatedBalance = wallet.deposit(depositMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }

    @Override
    public Money withdraw(Long walletId, String username, Money withdrawalMoney) throws UserNotFoundException, InsufficientBalanceException, AuthenticationFailed {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AuthenticationFailed("Username or password does not match."));
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException("Wallet Not Found"));
        Money updatedBalance = wallet.withdraw(withdrawalMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }


}