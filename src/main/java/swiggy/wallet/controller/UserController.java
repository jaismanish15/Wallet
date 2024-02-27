package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.exception.UserAlreadyPresentException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.model.TransactionResponse;
import swiggy.wallet.model.UserResponse;
import swiggy.wallet.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody User user) {
        try {
            UserResponse userResponse = userService.register(user);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (UserAlreadyPresentException e) {
            return new ResponseEntity<>(new UserResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> delete() throws UserNotFoundException {
        String message  = userService.delete();
        return new ResponseEntity<>(message, HttpStatus.ACCEPTED);
    }

    @PutMapping("/addWallet")
    public ResponseEntity<User> addWallet() {
        User user = userService.addWallet();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}