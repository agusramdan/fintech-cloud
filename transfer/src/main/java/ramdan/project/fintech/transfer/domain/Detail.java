package ramdan.project.fintech.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
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
    private Date date;
    private String account;
    private Double amount;
    private String remark1 = "";
    private String remark2 = "";
}
