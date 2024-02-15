package swiggy.wallet.repository;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import swiggy.wallet.entity.Wallet;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
}


