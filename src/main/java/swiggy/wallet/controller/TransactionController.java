package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.model.TransactionResponse;
import swiggy.wallet.service.TransactionService;

@RestController
@RequestMapping("")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transact")
    public ResponseEntity<TransactionResponse> transact(@RequestBody TransactionRequest request) throws InsufficientBalanceException, InvalidAmountException, UserNotFoundException {
        String response = transactionService.transact(request);
        return new ResponseEntity<>(new TransactionResponse(response), HttpStatus.ACCEPTED);
    }
}
