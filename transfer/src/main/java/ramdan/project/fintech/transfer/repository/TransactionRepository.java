package ramdan.project.fintech.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramdan.project.fintech.transfer.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String> {

}
