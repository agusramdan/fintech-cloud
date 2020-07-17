package ramdan.project.fintech.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.DetailId;

public interface DetailRepository extends JpaRepository<Detail, DetailId> {
}
