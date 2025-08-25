package tt2.int221.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tt2.int221.backend.dto.CreateUserDTO;
import tt2.int221.backend.dto.CreateUserResponseDTO;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/v2/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Create new user w/ files", description = "Register a new user with national ID images")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Email already in use")
    @ApiResponse(responseCode = "500", description = "User create failed")
    public ResponseEntity<CreateUserResponseDTO> registerUser(
            @Valid @ModelAttribute CreateUserDTO userForm,
            @RequestParam(required = false) MultipartFile idCardImageFront,
            @RequestParam(required = false) MultipartFile idCardImageBack
    ) throws IOException {

        User createdUser = userService.createUser(userForm, idCardImageFront, idCardImageBack);
        CreateUserResponseDTO response = CreateUserResponseDTO.from(createdUser);
        return ResponseEntity.status(201).body(response);
    }
}
