package ramdan.project.fintech.transfer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
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
    private Date date;
    private String remark1;
    private String remark2;
}
