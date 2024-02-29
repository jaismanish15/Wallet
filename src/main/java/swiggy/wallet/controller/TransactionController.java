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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("")
    public ResponseEntity<TransactionResponse> transact(@RequestBody TransactionRequest request) {
        try {
            TransactionResponse response = transactionService.transact(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransactionResponse(e.getMessage()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransactionResponse("User not found"));
        } catch (InvalidAmountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TransactionResponse("Service Fee is Greater than Transaction Amount"));
        }
    }

//    @GetMapping
//    public ResponseEntity<List<TransactionResponse>> fetch(@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate){
//        if(startDate != null && endDate != null)
//            return new ResponseEntity<>(transactionService.fetchByDate(startDate,endDate), HttpStatus.OK);
//        return new ResponseEntity<>(transactionService.fetch(), HttpStatus.OK);
//    }
}
