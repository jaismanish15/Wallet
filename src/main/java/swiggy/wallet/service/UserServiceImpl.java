package swiggy.wallet.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.exception.UserAlreadyPresentException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.UserResponse;
import swiggy.wallet.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(User user) throws UserAlreadyPresentException {
        if(userRepository.findByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyPresentException("User Already Registered");
        User registeredUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
        userRepository.save(registeredUser);
        return new UserResponse(registeredUser.getId(), registeredUser.getUsername(), registeredUser.getWallet(), "User Registered Successfully");
    }

    @Override
    public String delete() throws UserNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userToDelete = userRepository.findByUsername(username);
        if(userToDelete.isEmpty())
            throw new UserNotFoundException("User not found");

        userRepository.delete(userToDelete.get());
        return "User Deleted Successfully";
    }
}