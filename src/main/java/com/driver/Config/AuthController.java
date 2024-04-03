package com.driver.Config;

import com.driver.Config.JWTSecurity.JwtService;
import com.driver.Models.AdminInfo;
import com.driver.Models.JwtRequest;
import com.driver.Models.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    // Endpoint for user registration (signup)
    @PostMapping("/signup")
    public ResponseEntity<?> addAdmin(@RequestBody AdminInfo adminInfo) {
        try{
            // Check if the user with the provided email already exists
            Optional<AdminInfo> adminInfoOpt = authRepository.findByEmail(adminInfo.getEmail());
            if(adminInfoOpt.isPresent()) {
                throw new RuntimeException("User already Present with email: " + adminInfo.getEmail());
            }
            // Encode the password before saving it to the database
            adminInfo.setPassword(passwordEncoder.encode(adminInfo.getPassword()));

            // Save the user information to the database
            authRepository.save(adminInfo);

            return new ResponseEntity<>(adminInfo, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        // Authenticate user credentials
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // Generate JWT token for the authenticated user
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtService.generateToken(userDetails);

        // Create and return JWT response containing the token
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Helper method to perform authentication
    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            // Use the AuthenticationManager to authenticate the user
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            // Throw exception if credentials are invalid
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
}
