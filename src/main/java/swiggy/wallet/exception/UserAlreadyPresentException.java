package swiggy.wallet.exception;

import org.apache.coyote.BadRequestException;

public class UserAlreadyPresentException extends BadRequestException {

    public UserAlreadyPresentException(String message) {
        super(message);
    }
}