package ramdan.project.fintech.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailDto {
    private String number;
    private Date date;
    private String account;
    private Double amount;
    private Double balance;
    private String remark1;
    private String remark2;
}
