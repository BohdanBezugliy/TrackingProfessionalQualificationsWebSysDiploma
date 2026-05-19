package com.BezuhlyiBohdanK22_1.qualitrack.service;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserRegistrationDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.UserShowDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.UserEntity;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

public interface IUserService {
    void save(UserRegistrationDto user);
    UserShowDto getUserByEmail(String email);
}
