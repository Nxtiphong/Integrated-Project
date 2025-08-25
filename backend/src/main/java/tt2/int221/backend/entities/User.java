package tt2.int221.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tt2.int221.backend.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 40)
    private String fullName;

    @Column
    private String phoneNumber;

    @Column
    private String bankAccount;

    @Column
    private String idCardNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role userType;

    @Column(nullable = false)
    private Boolean isActive = false;

    @Column
    private String idCardImageFront;

    @Column
    private String idCardImageBack;
}
