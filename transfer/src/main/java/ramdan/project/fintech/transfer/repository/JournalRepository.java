package ramdan.project.fintech.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramdan.project.fintech.transfer.domain.Journal;

public interface JournalRepository extends JpaRepository<Journal, String> {

}
