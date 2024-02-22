package swiggy.wallet.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String message) {
        super(message);
    }
}