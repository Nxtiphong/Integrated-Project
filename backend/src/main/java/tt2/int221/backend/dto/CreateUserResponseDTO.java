package tt2.int221.backend.dto;

import lombok.Data;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.enums.Role;

import java.io.File;

@Data
public class CreateUserResponseDTO {
    private Integer id;
    private String nickName;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Boolean isActive;
    private Role userType;

    public static CreateUserResponseDTO from(User u) {
        CreateUserResponseDTO dto = new CreateUserResponseDTO();
        dto.setId(u.getId());
        dto.setNickName(u.getNickName());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setPhoneNumber(u.getPhoneNumber());
        dto.setIsActive(u.getIsActive());
        dto.setUserType(u.getUserType());
        return dto;
    }
}
