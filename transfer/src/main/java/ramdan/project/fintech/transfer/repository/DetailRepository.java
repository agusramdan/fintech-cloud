package ramdan.project.fintech.transfer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ramdan.project.fintech.transfer.domain.Detail;
import ramdan.project.fintech.transfer.domain.DetailId;

import java.util.Date;
import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, DetailId> {
    @Query(value = "from Detail where account = :account and date between :fr and :to order by date , number,idx"
            , countQuery = "select count(*) from Detail where account = :account and date between :fr and :to "
    )
    Page<Detail> findAllByAccount(String account, Date fr, Date to, Pageable pageable);

    @Query("from Detail where number = :number order by idx")
    List<Detail> findAllByJournal(String number);
}
