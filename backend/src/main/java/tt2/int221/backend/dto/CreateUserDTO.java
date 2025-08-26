package tt2.int221.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.enums.Role;

@Data
public class CreateUserDTO {
    @NotBlank
    private String nickName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password ต้องยาวอย่างน้อย 8 ตัวอักษร")
    @Pattern(
            regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message="Password ต้องมี a-z, A-Z, ตัวเลข และอักขระพิเศษ"
    )
    private String password;

    @NotBlank
    @Size(min = 4, max = 40)
    private String fullName;

    @NotNull
    private Role userType;
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String idCardNumber;
    private MultipartFile idCardImageFront;
    private MultipartFile idCardImageBack;

    @AssertTrue(message = "เมื่อ userType=SELLER ต้องกรอก phoneNumber, bankAccount, idCardNumber และแนบไฟล์รูปบัตร")
    public boolean isSellerFieldsPresentIfSeller() {
        if (userType != Role.SELLER) return true;
        return isNotBlank(phoneNumber)
                && isNotBlank(bankAccount)
                && isNotBlank(bankName)
                && isNotBlank(idCardNumber);
    }

    private boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }
}
