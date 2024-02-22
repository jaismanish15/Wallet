package swiggy.wallet.service;

import swiggy.wallet.entity.User;
import swiggy.wallet.exception.UserAlreadyPresentException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.UserResponse;

public interface UserService {
    UserResponse register(User user) throws UserAlreadyPresentException;
    String delete() throws UserNotFoundException;

}
