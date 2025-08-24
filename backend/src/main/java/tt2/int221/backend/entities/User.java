package tt2.int221.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import tt2.int221.backend.enums.Role;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nickname",nullable = false)
    @NotNull
    private String nickname;

    @Column(name = "email" ,nullable = false, unique = true)
    @NotNull
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8)
    @NotNull
    private String password;

    @Column(name = "fullName", nullable = false, length = 40)
    @NotNull
    @Size(min = 4, max = 40)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "role",nullable = false)
    private Role role;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "bankName")
    private String bankName;

    @Column(name = "bankAccountNumber")
    private String bankAccountNumber;

    @Column(name = "nationalIdNumber")
    private String nationalIdNumber;

    @Column(name = "nationalFrontUrl")
    private String nationalFrontUrl;

    @Column(name = "nationalBackUrl")
    private String nationalBackUrl;

}
