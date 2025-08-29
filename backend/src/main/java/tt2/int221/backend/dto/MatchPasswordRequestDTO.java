package tt2.int221.backend.dto;

import lombok.Data;

@Data
public class MatchPasswordRequestDTO {
    private String email;
    private String password;
}
