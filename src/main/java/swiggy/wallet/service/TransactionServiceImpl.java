package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.InsufficientBalanceException;
import swiggy.wallet.exception.InvalidAmountException;
import swiggy.wallet.exception.UnauthorizedWalletException;
import swiggy.wallet.exception.UserNotFoundException;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Override
    public String transact(TransactionRequest requestModel) throws UserNotFoundException, InsufficientBalanceException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            throw new UserNotFoundException("User Not Found");
        }
        User sender = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User "+ user.getUsername() + " not found."));
        User receiver = userRepository.findById(requestModel.getReceiverId()).orElseThrow(() -> new UserNotFoundException("User with Id: "+ requestModel.getReceiverId() + " not found."));
        Wallet senderWallet = walletRepository.findByIdAndUser(user.getId(), user)
                .orElseThrow();
        Wallet recieverWallet = walletRepository.findByIdAndUser(requestModel.getReceiverId(), receiver)
                .orElseThrow();

        try{
            senderWallet.withdraw(requestModel.getMoney());
        } catch (Exception e){
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        recieverWallet.deposit(requestModel.getMoney());

        userRepository.save(sender);
        userRepository.save(receiver);

        return "Transaction Successful";
    }
}
