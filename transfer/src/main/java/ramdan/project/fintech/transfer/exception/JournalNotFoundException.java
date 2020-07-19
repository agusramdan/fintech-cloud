package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Journal not found.")
public class JournalNotFoundException extends RuntimeException {
    public JournalNotFoundException() {
    }
}
