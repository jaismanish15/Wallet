package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.service.WalletService;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<Wallet> createWallet() {
        try {
            Wallet wallet = walletService.create();
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Money> getWallet(@PathVariable Long walletId) {
        try {
            Money balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<Money> deposit(@PathVariable Long walletId, @RequestBody Money depositMoney) {
        try {
            Money updatedBalance = walletService.deposit(walletId, depositMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<Money> withdraw(@PathVariable Long walletId, @RequestBody Money withdrawalMoney) {
        try {
            Money updatedBalance = walletService.withdraw(walletId, withdrawalMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Wallet>> fetchAll() {
        try {
            List<Wallet> wallets = walletService.fetchAll();
            return ResponseEntity.ok(wallets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}