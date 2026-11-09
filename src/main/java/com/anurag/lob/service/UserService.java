package com.anurag.lob.service;

import com.anurag.lob.dto.LoginRequestDTO;
import com.anurag.lob.dto.LoginResponseDTO;
import com.anurag.lob.dto.RegisterRequestDTO;
import com.anurag.lob.exception.DuplicateUserException;
import com.anurag.lob.exception.InvalidCredentialsException;
import com.anurag.lob.exception.UserNotFoundException;
import com.anurag.lob.repository.UserRepository;
import com.anurag.lob.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.anurag.lob.entity.User;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder   passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public void registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already registered");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("Username already registered");
        }
        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

    }

    public LoginResponseDTO loginUser(LoginRequestDTO request){
        Optional<User> userOptional=
                userRepository.findByEmail(request.getEmail());

        User user = userOptional.orElseThrow(
                () -> new UserNotFoundException("Email not found")
        );
        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())
        ){
            throw new InvalidCredentialsException("Please enter correct password");
        }
        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponseDTO(
                "Login Successful",
                user.getUsername(),
                token
        );

    }
}
