package tt2.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import tt2.int221.backend.enums.Role;

@Data
public class CreateUserDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "password ต้องยาวอย่างน้อย 8 ตัวอักษร")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "password ต้องมี a-z, A-Z, ตัวเลข และอักขระพิเศษ"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    @Size(min = 4, max = 40)
    private String fullName;

    @NotNull
    private Role role;

    //role = SELLER
    private String mobileNumber;
    private String bankName;
    private String bankAccountNumber;
    private String nationalIdNumber;
    private String nationalFrontUrl;
    private String nationalBackUrl;

    @AssertTrue(message = "เมื่อ role=SELLER ต้องกรอก: mobileNumber, bankName, bankAccountNumber, nationalIdNumber, nationalFrontUrl, nationalBackUrl")
    public boolean isSellerFieldsPresentIfSeller() {
        if (role != Role.SELLER) return true;
        return isNotBlank(mobileNumber)
                && isNotBlank(bankName)
                && isNotBlank(bankAccountNumber)
                && isNotBlank(nationalIdNumber);
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
