package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserShowDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.enums.UserRole;
import com.BezuhlyiBohdanK22_1.qualitrack.exception.UserNotFoundException;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserEntity UserRegistrationDtoToEntity(UserRegistrationDto userRegistrationDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.USER);
        userEntity.setEmail(userRegistrationDto.email());
        userEntity.setPasswordHash(passwordEncoder.encode(userRegistrationDto.password()));
        userEntity.setUserRole(userRegistrationDto.role());
        return userEntity;
    }

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(UserRegistrationDto user) {
        UserEntity userEntity = UserRegistrationDtoToEntity(user);
        userRepository.save(userEntity);
    }

    @Override
    public UserShowDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntity -> new UserShowDto(userEntity.getEmail()))
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}
