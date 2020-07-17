package ramdan.project.fintech.transfer.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity @IdClass(DetailId.class)
public class Detail implements Serializable {
    @Id
    private String number;
    @Id
    private int idx;
    private Date date;
    private String account;
}
