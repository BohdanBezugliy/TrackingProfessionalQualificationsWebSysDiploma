package com.BezuhlyiBohdanK22_1.qualitrack.initializer;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserShowDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.enums.UserRole;
import com.BezuhlyiBohdanK22_1.qualitrack.exception.UserNotFoundException;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UserRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final PasswordEncoder encoder;
    private final UserService userService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String ... args) {
        try {
            UserShowDto user = userService.getUserByEmail(adminEmail);
        }catch (UserNotFoundException e){
            UserRegistrationDto admin = new UserRegistrationDto(adminEmail, adminPassword, UserRole.ADMIN);
            userService.save(admin);
        }
    }
}
