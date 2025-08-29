package tt2.int221.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tt2.int221.backend.config.ConfigJWT;
import tt2.int221.backend.dto.MatchPasswordRequestDTO;
import tt2.int221.backend.dto.MatchPasswordResponseDTO;
import tt2.int221.backend.entities.User;
import tt2.int221.backend.repositories.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public MatchPasswordResponseDTO login(MatchPasswordRequestDTO dto) {
        userService.matchPassword(dto);

        User user = userRepository.findByEmail(dto.getEmail());

        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You need to activate your account before signing in.");
        }

        String accessToken = ConfigJWT.generateToken(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getUserType().name(),
                30 * 60 * 1000 // 30 นาที
        );

        String refreshToken = ConfigJWT.generateToken(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getUserType().name(),
                24 * 60 * 60 * 1000 // 24 ชั่วโมง
        );

        MatchPasswordResponseDTO response = new MatchPasswordResponseDTO();
        response.setAccess_token(accessToken);
        response.setRefresh_token(refreshToken);
        response.setNickname(user.getNickName());

        return response;
    }
}
