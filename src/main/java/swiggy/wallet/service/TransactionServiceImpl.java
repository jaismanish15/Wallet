package swiggy.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;
import swiggy.wallet.exception.*;
import swiggy.wallet.model.TransactionRequest;
import swiggy.wallet.model.TransactionResponse;
import swiggy.wallet.repository.UserRepository;
import swiggy.wallet.repository.WalletRepository;
import swiggy.wallet.valueObject.Money;

import java.time.LocalDateTime;

import static swiggy.wallet.Constant.SERVICE_FEE;

@Service
public class TransactionServiceImpl implements TransactionService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Override
    public TransactionResponse transact(TransactionRequest requestModel) throws UserNotFoundException, InsufficientBalanceException, InvalidAmountException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            throw new UserNotFoundException("User Not Found");
        }
        User sender = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User "+ username + " not found."));
        User receiver = userRepository.findById(requestModel.getReceiverId()).orElseThrow(() -> new UserNotFoundException("User "+ requestModel.getReceiverId() + " not found."));
        Wallet senderWallet = walletRepository.findById(requestModel.getSenderWalletId()).orElseThrow(()-> new WalletNotFoundException("Sender Wallet Not Found"));
        Wallet receiverWallet = walletRepository.findById(requestModel.getReceiverWalletId()).orElseThrow(()-> new WalletNotFoundException("Receiver Wallet Not Found"));
        Money serviceFee = getServiceFee(requestModel, receiverWallet, senderWallet);
        senderWallet.withdraw(serviceFee);
        try{
            senderWallet.withdraw(requestModel.getMoney());
        } catch (Exception e){
            throw new InsufficientBalanceException("Insufficient Balance");
        }

        receiverWallet.deposit(requestModel.getMoney());

        userRepository.save(sender);
        userRepository.save(receiver);


        return new TransactionResponse(
                "Transaction Successful",
                LocalDateTime.now(),
                requestModel.getSenderWalletId(),
                requestModel.getReceiverWalletId(),
                requestModel.getMoney(), serviceFee
        );
    }

    private static Money getServiceFee(TransactionRequest requestModel, Wallet receiverWallet, Wallet senderWallet) throws InvalidAmountException {
        Money serviceFee = new Money();
        if(!requestModel.getMoney().getCurrency().equals(receiverWallet.getMoney().getCurrency()) || !requestModel.getMoney().getCurrency().equals(senderWallet.getMoney().getCurrency())){
            serviceFee.setAmount(SERVICE_FEE.getAmount());
            serviceFee.setCurrency(SERVICE_FEE.getCurrency());
        }
        if(serviceFee.getAmount().doubleValue() >= requestModel.getMoney().getAmount().doubleValue())
            throw new InvalidAmountException("");
        return serviceFee;
    }
}
