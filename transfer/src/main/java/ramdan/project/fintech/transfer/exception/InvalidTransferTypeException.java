package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid transfer type.")
public class InvalidTransferTypeException extends RuntimeException {
    public InvalidTransferTypeException() {
    }
}
