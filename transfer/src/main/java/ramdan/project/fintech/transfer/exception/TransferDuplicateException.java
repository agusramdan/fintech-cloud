package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Journal not found.")
public class TransferDuplicateException extends RuntimeException {
    public TransferDuplicateException() {
        super(null,null,false,false);
    }
}
