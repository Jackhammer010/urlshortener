package com.jackhammer.url.shortener.service;

import com.jackhammer.url.shortener.dtos.LoginRequest;
import com.jackhammer.url.shortener.models.User;
import com.jackhammer.url.shortener.repository.UserRepository;
import com.jackhammer.url.shortener.security.JwtAuthenticationResponse;
import com.jackhammer.url.shortener.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwtToken);
    }
}
