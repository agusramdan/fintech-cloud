package ramdan.project.fintech.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JournalDto {
    private String number;
    private Date date;
    private String reversal;
    private Date reversalDate;
    private BigDecimal amount;
    private String remark1;
    private String remark2;
    private DetailDto[] details;
}
