package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.repository.UserRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public String transact(TransactionRequest requestModel) throws UserNotFoundException, InsufficientBalanceException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User "+ username + " not found."));
        User receiver = userRepository.findById(requestModel.getReceiverId()).orElseThrow(() -> new UserNotFoundException("User with Id: "+ requestModel.getReceiverId() + " not found."));

        try{
            sender.getWallet().withdraw(requestModel.getMoney());
        } catch (Exception e){
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        receiver.getWallet().deposit(requestModel.getMoney());

        userRepository.save(sender);
        userRepository.save(receiver);

        return "Transaction Successful";
    }
}
