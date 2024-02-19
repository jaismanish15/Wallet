package swiggy.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import swiggy.wallet.entity.User;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.model.TransactionResponse;
import swiggy.wallet.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            User errorUser = new User();
            errorUser.setUsername("Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorUser);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteUser() throws Exception {
        this.userService.delete();
        return ResponseEntity.ok("User Deleted successfully");
    }


}