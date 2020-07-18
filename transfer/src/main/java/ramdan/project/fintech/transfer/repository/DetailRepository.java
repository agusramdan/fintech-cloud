package ramdan.project.fintech.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.DetailId;

import java.util.Date;
import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, DetailId> {
    @Query("from Detail where account = :account and date between :fr and :to order by date , number,idx")
    List<Detail> findAllByAccount(String account, Date fr, Date to);
}
