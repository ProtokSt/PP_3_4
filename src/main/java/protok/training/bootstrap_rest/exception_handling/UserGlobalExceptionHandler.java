package protok.training.bootstrap_rest.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException(DBSomeException exception) {
        UserIncorrectData uID = new UserIncorrectData();
        uID.setInfo(exception.getMessage());
        return new ResponseEntity<>(uID, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectData> handleException( Exception exception) {
        UserIncorrectData uID = new UserIncorrectData();
        uID.setInfo(exception.getMessage());
        return new ResponseEntity<>(uID, HttpStatus.BAD_REQUEST);
    }
}
