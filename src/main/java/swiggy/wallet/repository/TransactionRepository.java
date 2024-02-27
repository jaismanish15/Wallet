package swiggy.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swiggy.wallet.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
