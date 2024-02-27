package swiggy.wallet.repository;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import swiggy.wallet.entity.User;
import swiggy.wallet.entity.Wallet;

import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByIdAndUser(Long id, User user);
}


