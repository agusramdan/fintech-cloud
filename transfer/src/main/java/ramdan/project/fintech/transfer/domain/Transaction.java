package ramdan.project.fintech.transfer.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Transaction implements Serializable {
    @Id
    private String number;
    private Date  date;
    private String remark1;
    private String remark2;

}
