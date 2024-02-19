package swiggy.wallet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userToDelete = userRepository.findByUsername(username);
        if(userToDelete.isEmpty())
            throw new Exception("User could not be found.");

        userRepository.delete(userToDelete.get());

    }
}