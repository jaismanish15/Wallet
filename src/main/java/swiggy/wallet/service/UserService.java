package swiggy.wallet.service;

import swiggy.wallet.entity.User;

public interface UserService {
    User register(User user);
    void delete() throws Exception;

}
