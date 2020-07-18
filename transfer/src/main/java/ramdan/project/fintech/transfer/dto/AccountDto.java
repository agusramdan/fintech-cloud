package ramdan.project.fintech.transfer.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto implements Serializable {
    private String number;
    private Long version;
    private String name;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal balance;
    @JsonDeserialize(using = MoneyDeserializer.class)
    private BigDecimal limit;
}
