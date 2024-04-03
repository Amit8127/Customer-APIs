package com.driver.Config;

import com.driver.Config.AuthRepository;
import com.driver.Models.AdminInfo;
import com.driver.Models.AdminInfoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private AuthRepository authRepository;

    // This method is called by Spring Security to load user details based on the provided email.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Retrieve user information from the database based on the provided email.
        Optional<AdminInfo> userInfo = authRepository.findByEmail(email);

        // If user information is found, create and return UserDetails using AdminInfoUserDetails.
        return userInfo.map(AdminInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
    }
}
