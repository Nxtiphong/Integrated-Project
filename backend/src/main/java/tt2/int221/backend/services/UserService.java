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
                           MultipartFile nationalFrontFile,
                           MultipartFile nationalBackFile) throws IOException {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
        }

        User u = new User();
        u.setNickname(dto.getNickname());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword()); // ยังไม่ hash รอ sprint ต่อไป
        u.setFullName(dto.getFullName());
        u.setRole(dto.getRole());

        if (dto.getRole() == Role.SELLER) {
            if (nationalFrontFile == null || nationalFrontFile.isEmpty()
                    || nationalBackFile == null || nationalBackFile.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Seller ต้องอัปโหลดไฟล์บัตรประชาชนทั้งด้านหน้าและด้านหลัง");
            }
        }

        if (dto.getRole() == Role.SELLER) {
            u.setMobileNumber(dto.getMobileNumber());
            u.setBankName(dto.getBankName());
            u.setBankAccountNumber(dto.getBankAccountNumber());
            u.setNationalIdNumber(dto.getNationalIdNumber());

            if (nationalFrontFile != null && !nationalFrontFile.isEmpty()) {
                // ตั้งชื่อไฟล์เอง
                String fileName = "nid_front_" + System.currentTimeMillis() +
                        getExtension(nationalFrontFile.getOriginalFilename());
                String storedName = fileService.saveUserFile(nationalFrontFile, fileName);
                u.setNationalFrontUrl(storedName);
            }

            if (nationalBackFile != null && !nationalBackFile.isEmpty()) {
                String fileName = "nid_back_" + System.currentTimeMillis() +
                        getExtension(nationalBackFile.getOriginalFilename());
                String storedName = fileService.saveUserFile(nationalBackFile, fileName);
                u.setNationalBackUrl(storedName);
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
