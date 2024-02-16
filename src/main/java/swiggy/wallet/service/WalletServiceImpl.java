package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.repository.WalletRepository;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;


    @Override
    public Wallet create() {
        Wallet wallet = new Wallet(new Money());
        walletRepository.save(wallet);
        return wallet;
    }
    @Override
    public Money getBalance(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"))
                .getMoney();
    }

    @Override
    public Money deposit(Long walletId, Money depositMoney) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Money updatedBalance = wallet.deposit(depositMoney);
        wallet.setMoney(updatedBalance);
        walletRepository.save(wallet);

        return updatedBalance;
    }

    @Override
    public Money withdraw(Long walletId, Money withdrawalMoney) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        Money updatedBalance = wallet.withdraw(withdrawalMoney);
        wallet.setMoney(updatedBalance);
        walletRepository.save(wallet);

        return updatedBalance;
    }

    @Override
    public List<Wallet> fetchAll() {
        return walletRepository.findAll();
    }

}