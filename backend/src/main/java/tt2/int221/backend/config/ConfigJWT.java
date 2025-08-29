package tt2.int221.backend.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class ConfigJWT {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String ISSUER = "http://intproj24.sit.kmutt.ac.th/tt2/";

    public static String generateToken(Integer userId, String email, String nickname, String role, long durationMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + durationMillis);

        return Jwts.builder()
                .setIssuer(ISSUER)                 // iss
                .setIssuedAt(now)                  // iat
                .setExpiration(exp)                // exp
                .claim("id", userId)               // id
                .claim("email", email)             // email
                .claim("nickname", nickname)       // nickname
                .claim("role", role)               // role
                .signWith(key)
                .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("id", Integer.class);
    }

    public static String getRoleFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }
}
