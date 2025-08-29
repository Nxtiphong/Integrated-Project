package tt2.int221.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tt2.int221.backend.config.ConfigJWT;
import tt2.int221.backend.dto.CreateUserDTO;
import tt2.int221.backend.dto.MatchPasswordRequestDTO;
import tt2.int221.backend.dto.MatchPasswordResponseDTO;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.enums.Role;
import tt2.int221.backend.exceptions.NotfoundException;
import tt2.int221.backend.repositories.UserRepository;
import tt2.int221.backend.utils.HashUtil;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public User createUser(CreateUserDTO dto,
                           MultipartFile idCardImageFront,
                           MultipartFile idCardImageBack) throws IOException {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        User u = new User();
        u.setNickName(dto.getNickName());
        u.setEmail(dto.getEmail());
        u.setPassword(HashUtil.sha256(dto.getPassword()));
        u.setFullName(dto.getFullName());
        u.setUserType(dto.getUserType());
        u.setIsActive(false);

        if (dto.getUserType() == Role.SELLER) {
            if (idCardImageFront == null || idCardImageFront.isEmpty()
                    || idCardImageBack == null || idCardImageBack.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Seller ต้องอัปโหลดไฟล์บัตรประชาชนทั้งด้านหน้าและด้านหลัง");
            }

            u.setPhoneNumber(dto.getPhoneNumber());
            u.setBankAccount(dto.getBankAccount());
            u.setBankName(dto.getBankName());
            u.setIdCardNumber(dto.getIdCardNumber());

            if (!idCardImageFront.isEmpty()) {
                String fileName = "nid_front_" + System.currentTimeMillis() +
                        getExtension(idCardImageFront.getOriginalFilename());
                String storedName = fileService.saveUserFile(idCardImageFront, fileName);
                u.setIdCardImageFront(storedName);
            }

            if (!idCardImageBack.isEmpty()) {
                String fileName = "nid_back_" + System.currentTimeMillis() +
                        getExtension(idCardImageBack.getOriginalFilename());
                String storedName = fileService.saveUserFile(idCardImageBack, fileName);
                u.setIdCardImageBack(storedName);
            }
        }

        User saveUser = userRepository.save(u);


        String verifyToken = ConfigJWT.generateToken(
                saveUser.getId(),
                saveUser.getEmail(),
                saveUser.getNickName(),
                saveUser.getUserType().name(),
                24 * 60 * 60 * 1000
        );
        saveUser.setLatestVerifyToken(verifyToken);
        userRepository.save(saveUser);

        emailService.sendVerificationEmail(
                saveUser.getEmail(),
                saveUser.getNickName(),
                baseUrl + "/tt2/verify-email?token=" + verifyToken
        );

        return saveUser;
    }

    private String getExtension(String originalName) {
        if (originalName != null && originalName.contains(".")) {
            return originalName.substring(originalName.lastIndexOf("."));
        }
        return "";
    }

    public String matchPassword(MatchPasswordRequestDTO dto) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is empty");
        }

        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is empty");
        }

        User existsUser = userRepository.findByEmail(dto.getEmail());

        if (existsUser == null) {
            throw new NotfoundException("No user found with email: " + dto.getEmail());
        }

        String hashedPassword = existsUser.getPassword();

        String loginPassword = HashUtil.sha256(dto.getPassword());

        if(!hashedPassword.equals(loginPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect Password, Try again");
        }

        return "Password matched";
    }
}
