package com.room_rent.Room_Rent_Application.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
    private boolean enabled = true;

    //for otp
    private String otp;
    @Column(name = "is_otp_verified")
    private boolean isOtpVerified = false;

    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;




}
