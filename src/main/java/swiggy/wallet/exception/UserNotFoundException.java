package swiggy.wallet.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

public class UserNotFoundException extends NullPointerException {

    public UserNotFoundException(String message) {
        super(message);
    }
}