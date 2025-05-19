package tt2.int221.backend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tt2.int221.backend.dto.ExceptionDTO;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(NotfoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(NotfoundException ex, HttpServletRequest request) {
        ExceptionDTO error = new ExceptionDTO(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        ExceptionDTO error = new ExceptionDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionDTO> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        ExceptionDTO error = new ExceptionDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BadRequest",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
