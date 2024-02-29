package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.enums.IntraWalletTransactionType;
import swiggy.wallet.model.IntraWalletTransactionResponse;
import swiggy.wallet.valueObject.Money;
import swiggy.wallet.service.WalletService;

@RestController
@RequestMapping("/api/user/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @PostMapping(value = "/{walletId}/intra-wallet-transactions", params = "type=DEPOSIT")
    public ResponseEntity<IntraWalletTransactionResponse> deposit(@PathVariable("walletId") Long walletId, @RequestBody Money depositMoney) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Money updatedBalance = walletService.deposit(walletId, username, depositMoney);
            return ResponseEntity.ok(new IntraWalletTransactionResponse(walletId, updatedBalance, "Deposit Successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/{walletId}/intra-wallet-transactions", params = "type=WITHDRAW")
    public ResponseEntity<IntraWalletTransactionResponse> withdraw(@PathVariable("walletId") Long walletId, @RequestBody Money withdrawalMoney) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Money updatedBalance = walletService.withdraw(walletId, username, withdrawalMoney);
            return ResponseEntity.ok(new IntraWalletTransactionResponse(walletId, updatedBalance, "Withdrawal Successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}