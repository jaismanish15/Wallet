package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.service.WalletService;

import java.util.List;

@RestController
@RequestMapping("/api/user/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("{walletId}/deposit")
    public ResponseEntity<Money> deposit(@PathVariable("walletId") Long walletId, @RequestBody Money depositMoney) {
        try {
            Money updatedBalance = walletService.deposit(walletId, depositMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("{walletId}/withdraw")
    public ResponseEntity<Money> withdraw(@PathVariable("walletId") Long walletId, @RequestBody Money withdrawalMoney) {
        try {
            Money updatedBalance = walletService.withdraw(walletId, withdrawalMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}