package ramdan.project.fintech.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class OptimisticLockingFailure  extends RuntimeException{
    public OptimisticLockingFailure() {
    }

    public OptimisticLockingFailure(String s) {
        super(s);
    }

    public OptimisticLockingFailure(String s, Throwable throwable) {
        super(s, throwable);
    }

    public OptimisticLockingFailure(Throwable throwable) {
        super(throwable);
    }

    public OptimisticLockingFailure(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
