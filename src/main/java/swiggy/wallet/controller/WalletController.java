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
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping("/deposit")
    public ResponseEntity<Money> deposit(@RequestBody Money depositMoney) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Money updatedBalance = walletService.deposit(username, depositMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Money> withdraw(@RequestBody Money withdrawalMoney) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Money updatedBalance = walletService.withdraw(username, withdrawalMoney);
            return ResponseEntity.ok(updatedBalance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}