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
public class Journal implements Serializable {
    @Id
    private String number;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "trx_time")
    private Date date;
    private String reversal; // reversal number
    @Column(name = "rev_time")
    private Date reversalDate; // reversal number

    private BigDecimal amount;
    private String remark1;
    private String remark2;
}
