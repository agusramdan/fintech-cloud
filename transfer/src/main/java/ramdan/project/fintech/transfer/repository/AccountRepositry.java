package ramdan.project.fintech.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramdan.project.fintech.transfer.domain.Account;

public interface AccountRepositry extends JpaRepository<Account, String> {

}
