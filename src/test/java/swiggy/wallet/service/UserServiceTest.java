package swiggy.wallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import swiggy.wallet.entity.User;
import swiggy.wallet.enums.Country;
import swiggy.wallet.enums.Currency;
import swiggy.wallet.exception.UserAlreadyPresentException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.UserResponse;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRegisterSuccess() throws UserAlreadyPresentException {
        User user = new User("testuser", "testpassword", Country.INDIA);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserResponse userResponse = userService.register(user);

        assertNotNull(userResponse);
        assertEquals("User Registered Successfully", userResponse.getMessage());
    }

    @Test
    public void testRegisterUserAlreadyPresent() {
        User user = new User("existinguser", "testpassword", Country.INDIA);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyPresentException.class, () -> userService.register(user));
    }

    @Test
    public void testDeleteSuccess() throws UserNotFoundException, UserAlreadyPresentException {
        User userToDelete = new User("testuser", "testpassword", Country.INDIA);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userToDelete));
        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String result = userService.delete();

        assertEquals("User Deleted Successfully", result);
    }

    @Test
    public void testDeleteUserNotFound() {
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UserNotFoundException.class, () -> userService.delete());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).delete(any());
    }

//    @Test
//    public void testAddWalletToUser() {
//        User user = new User("user1", "pass1", Country.INDIA, new ArrayList<>());
//        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
//        when(authentication.getName()).thenReturn("user1");
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        userService.addWallet(1);
//        userService.addWallet(1);
//
//        verify(userRepository, times(2)).findByUsername("user1");
//        verify(userRepository, times(2)).save(user);
//        assertEquals(2, user.getWallets().size() );
//        assertEquals(Currency.INR, user.getWallets().get(1).getMoney().getCurrency() );
//
//    }
}
