package tt2.int221.backend.dto;

import lombok.Data;

@Data
public class MatchPasswordResponseDTO {
    private String access_token;
    private String refresh_token;
    private String nickname;
}
