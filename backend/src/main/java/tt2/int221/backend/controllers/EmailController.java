package tt2.int221.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tt2.int221.backend.config.ConfigJWT;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.repositories.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/v2/users")
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        if (!ConfigJWT.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or expired token"));
        }

        String email = ConfigJWT.getEmailFromToken(token);
        Integer userId = ConfigJWT.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getIsActive()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already verified"));
        }

        user.setIsActive(true);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Your account has been successfully activated."));
    }
}
