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
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements Serializable {
    @Id
    private String number;
    @Version
    private Long version;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdate;
    private String name;
    private BigDecimal balance;
    private BigDecimal overdraft; // limit of credit limit negatif balance.
}
