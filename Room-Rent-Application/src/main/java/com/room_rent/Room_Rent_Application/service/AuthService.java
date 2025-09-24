package com.room_rent.Room_Rent_Application.service;

import com.room_rent.Room_Rent_Application.dto.AuthRequest;
import com.room_rent.Room_Rent_Application.dto.AuthResponse;
import com.room_rent.Room_Rent_Application.dto.RegisterRequest;
import com.room_rent.Room_Rent_Application.dto.VerifyOtpRequest;
import com.room_rent.Room_Rent_Application.model.PasswordResetToken;
import com.room_rent.Room_Rent_Application.model.Role;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.repository.PasswordResetTokenRepository;
import com.room_rent.Room_Rent_Application.repository.UserRepository;
import com.room_rent.Room_Rent_Application.security.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;


    public String register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User u = new User();
        u.setEmail(req.email());
        u.setName(req.name());
        u.setPassword(passwordEncoder.encode(req.password()));
        // Assign role from request (default USER if invalid)
        try {
            u.setRole(Role.valueOf(req.role()));
        } catch (IllegalArgumentException | NullPointerException e) {
            u.setRole(Role.ROLE_USER);
        }
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        u.setOtp(String.valueOf(otp));
        boolean otpVerified = false;
        u.setOtpVerified(otpVerified);
        u.setOtpGeneratedTime(LocalDateTime.now());  // otp expiration time is setting
        userRepository.save(u);
        try {
            emailService.sendOtpEmail(u.getEmail(), "Your One-Time Password", otp);
        } catch (MessagingException e) {
            // Handle email sending error
            return "Error sending OTP email: " + e.getMessage();
        }
        return "User registered successfully. Please check your email for the OTP.";
    }


    //verify otp logics
    public String verifyOtp(VerifyOtpRequest req) {
        User user = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // check expiry (60 sec)
        LocalDateTime expiryTime = user.getOtpGeneratedTime().plusSeconds(60);
        if (LocalDateTime.now().isAfter(expiryTime)) {
            return "OTP expired. Please request a new one.";
        }

        if (user.getOtp() != null && user.getOtp().equals(req.otp())) {
            user.setOtpVerified(true);
            user.setOtp(null); // clear OTP after verification
            userRepository.save(user);
            return "OTP verified successfully.";
        }

        throw new IllegalArgumentException("Invalid or expired OTP");
    }

    //resend the otp
    public String resendOtp(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // generate new OTP
        String newOtp = String.valueOf(1000 + new Random().nextInt(9000));
        user.setOtp(newOtp);  //replaces the old OTP in the DB.
        user.setOtpGeneratedTime(LocalDateTime.now()); //resets timer for new 60 sec.
        user.setOtpVerified(false);
        userRepository.save(user); //saves back into DB (updates the row instead of creating new).

        //Because you’re using user.getEmail() (which is the email stored in DB), the OTP always goes to that same email.
        try {
            emailService.sendOtpEmail(user.getEmail(), "Your New One-Time Password", Integer.parseInt(newOtp));
        } catch (MessagingException e) {
            return "Error sending OTP email: " + e.getMessage();
        }

        return "New OTP sent successfully to your email.";
    }



    public AuthResponse login(AuthRequest req) {
        String email = req.email().toLowerCase();

        // 1. Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. OTP verify
        if (!user.isOtpVerified()) {
            throw new IllegalStateException("Please verify OTP before login.");
        }

        // 3. Authenticate password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.password()));

        // 4. Generate tokens
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new AuthResponse(token, refreshToken);
    }




    //for the forget password --->
    public void createPasswordResetToken(String email) throws MessagingException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        tokenRepository.deleteByUserId(user.getId());
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusSeconds(60 * 5)); // 5 minutes
        tokenRepository.save(token);
//        String resetLink = String.format("%s/reset-password?token=%s", "http://localhost:8080/api/auth", token.getToken());
//        emailService.sendHtmlEmail(user.getEmail(), "Password Reset", "Click to reset your password: " + resetLink);
        // Build reset link
        String frontendResetPage = "http://localhost:8080/reset-password-page?token=" + token.getToken();
        emailService.sendHtmlEmail(user.getEmail(), "Password Reset", frontendResetPage);

    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = tokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (prt.getExpiryDate().isBefore(Instant.now())) throw new IllegalArgumentException("Token expired");
        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(prt);
    }


    //refresh token
    public AuthResponse refreshToken(String refreshToken) {
        // validate refresh token
        String email = jwtUtil.validateAndExtractSubject(refreshToken);
        // find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // generate new access token
        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        // return new pair (keep same refresh token or generate a new one, up to you)
        return new AuthResponse(newAccessToken, refreshToken);
    }


    public void logout(String refreshToken) {
        // Extract email/subject from refresh token
        String email = jwtUtil.validateAndExtractSubject(refreshToken);

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Delete refresh token from DB if stored
        tokenRepository.deleteByUserId(user.getId());

        // If not storing tokens in DB, you can maintain an in-memory blacklist
        // blacklist.add(refreshToken);
    }
//fk;nask;fnkasnfjaflkafjnasfjadfjfbadsjfjafnkdjsafnafjkdfjnsafna
}
