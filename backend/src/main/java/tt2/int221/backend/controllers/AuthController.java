package tt2.int221.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tt2.int221.backend.dto.MatchPasswordRequestDTO;
import tt2.int221.backend.dto.MatchPasswordResponseDTO;
import tt2.int221.backend.services.AuthService;

@RestController
@RequestMapping("/v2/users")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authentications")
    public ResponseEntity<MatchPasswordResponseDTO> authenticate(
            @RequestBody MatchPasswordRequestDTO dto) {

        MatchPasswordResponseDTO response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}
