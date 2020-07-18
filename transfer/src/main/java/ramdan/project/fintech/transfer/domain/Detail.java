package ramdan.project.fintech.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(DetailId.class)
public class Detail implements Serializable {
    @Id
    private String number;
    @Id
    private Integer idx;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trx_time")
    private Date date;
    private String account;
    private BigDecimal amount;
    private BigDecimal balance;
    private String remark1 = "";
    private String remark2 = "";
}
