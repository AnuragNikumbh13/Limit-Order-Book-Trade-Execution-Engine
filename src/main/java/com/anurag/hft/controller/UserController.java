package com.anurag.hft.controller;

import com.anurag.hft.dto.LoginRequestDTO;
import com.anurag.hft.dto.LoginResponseDTO;
import com.anurag.hft.dto.RegisterRequestDTO;
import com.anurag.hft.repository.UserRepository;
import com.anurag.hft.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/userRegistration")
    public void registerUser(
           @Valid @RequestBody RegisterRequestDTO request){
        userService.registerUser(request);
    }
    @PostMapping(path="/login")
    public LoginResponseDTO loginUser(
            @Valid @RequestBody LoginRequestDTO request){
       return  userService.loginUser(request);
    }
    }

