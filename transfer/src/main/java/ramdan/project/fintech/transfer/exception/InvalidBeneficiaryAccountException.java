package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid beneficiary account.")
public class InvalidBeneficiaryAccountException extends RuntimeException {
    public InvalidBeneficiaryAccountException() {
    }
}
