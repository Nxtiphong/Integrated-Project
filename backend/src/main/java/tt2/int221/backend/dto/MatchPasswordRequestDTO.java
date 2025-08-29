package tt2.int221.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MatchPasswordRequestDTO {
    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(max = 14, message = "Password must not exceed 14 characters")
    private String password;
}
