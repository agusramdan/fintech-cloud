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
public class JournalDto {
    private String number;
    private Date date;
    private String remark1;
    private String remark2;
    private DetailDto[] details;
}
