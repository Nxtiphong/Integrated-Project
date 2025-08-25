package tt2.int221.backend.dto;

import lombok.Data;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.enums.Role;

import java.io.File;

@Data
public class CreateUserResponseDTO {
    private Integer id;
    private String nickname;
    private String email;
    private String fullName;
    private Role role;
    private String mobileNumber;
    private String bankName;
    private String bankAccountNumber;
    private String nationalIdNumber;
    private String nationalFrontUrl;
    private String nationalBackUrl;

    public static CreateUserResponseDTO from(User user) {
        CreateUserResponseDTO dto = new CreateUserResponseDTO();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setBankName(user.getBankName());
        dto.setBankAccountNumber(user.getBankAccountNumber());
        dto.setNationalIdNumber(user.getNationalIdNumber());
        dto.setNationalFrontUrl(user.getNationalFrontUrl());
        dto.setNationalBackUrl(user.getNationalBackUrl());
        return dto;
    }
}
