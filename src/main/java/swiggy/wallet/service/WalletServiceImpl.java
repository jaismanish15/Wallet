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
    public Money deposit(Long walletId, Money depositMoney) throws UserNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findByIdAndUser(walletId, user).orElseThrow(() -> new UnauthorizedWalletException(""));
        Money updatedBalance = wallet.deposit(depositMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }

    @Override
    public Money withdraw(Long walletId, Money withdrawalMoney) throws UserNotFoundException, InsufficientBalanceException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        Wallet wallet = walletRepository.findByIdAndUser(walletId, user).orElseThrow(() -> new UnauthorizedWalletException(""));
        Money updatedBalance = wallet.deposit(withdrawalMoney);
        walletRepository.save(wallet);

        return updatedBalance;
    }


}