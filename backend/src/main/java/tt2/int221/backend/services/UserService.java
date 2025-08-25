package tt2.int221.backend.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tt2.int221.backend.dto.CreateUserDTO;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.enums.Role;
import tt2.int221.backend.repositories.UserRepository;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

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
        u.setPassword(dto.getPassword()); // ยังไม่ hash รอ sprint ต่อไป
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

        return userRepository.save(u);
    }

    private String getExtension(String originalName) {
        if (originalName != null && originalName.contains(".")) {
            return originalName.substring(originalName.lastIndexOf("."));
        }
        return "";
    }

}
