package ramdan.project.fintech.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReversalCommand {
    private Type type;
    private Status status;
    private String no;
    private String reversal; // reversal number
    private Date date;
    private String source;
    private String beneficiary;
    private BigDecimal amount;
    private String remark1;
    private String remark2;
}
