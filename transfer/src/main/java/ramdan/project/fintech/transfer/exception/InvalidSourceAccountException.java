package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid source account.")
public class InvalidSourceAccountException extends RuntimeException {
    public InvalidSourceAccountException() {
        super(null,null,false,false);
    }
}
