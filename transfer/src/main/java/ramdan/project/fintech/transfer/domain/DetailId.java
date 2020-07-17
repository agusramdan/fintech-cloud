package ramdan.project.fintech.transfer.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetailId implements Serializable {
    private String number;
    private int idx;
}
