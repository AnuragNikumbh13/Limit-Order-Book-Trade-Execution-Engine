package com.anurag.hft.service;

import com.anurag.hft.dto.RegisterRequestDTO;
import com.anurag.hft.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.anurag.hft.entity.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public void registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
        }
        if (userRepository.existsByUsername(request.getUsername())) {

        }
        User user=new User();
        user.setUsername(request.getUsername());


    }
}
