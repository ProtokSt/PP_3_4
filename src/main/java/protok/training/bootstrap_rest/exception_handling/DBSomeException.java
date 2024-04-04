package protok.training.bootstrap_rest.exception_handling;

import org.springframework.dao.DataIntegrityViolationException;

public class DBSomeException extends DataIntegrityViolationException {
    public DBSomeException(String msg) {
        super(msg);
    }
}
