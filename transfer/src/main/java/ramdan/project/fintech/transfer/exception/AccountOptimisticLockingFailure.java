package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Account change by another user.")
public class AccountOptimisticLockingFailure extends RuntimeException {
    public AccountOptimisticLockingFailure() {
    }
}
