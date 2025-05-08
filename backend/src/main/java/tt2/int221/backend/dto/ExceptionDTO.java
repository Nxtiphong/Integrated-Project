package tt2.int221.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private ZonedDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
