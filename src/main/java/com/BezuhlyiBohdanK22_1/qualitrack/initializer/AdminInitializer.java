package com.BezuhlyiBohdanK22_1.qualitrack.initializer;

import com.BezuhlyiBohdanK22_1.qualitrack.repository.UserRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;


    public AdminInitializer(UserService userService, UserRepository userRepository, PasswordEncoder encoder,
                            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String ... args) {
        if (userRepository.findAll().isEmpty()) {
            userService.saveUser(adminEmail, adminPassword, "ADMIN");
        }
    }
}
