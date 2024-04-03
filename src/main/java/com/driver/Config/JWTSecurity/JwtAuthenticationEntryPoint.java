package com.driver.Config.JWTSecurity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * This class handles the entry point for unauthorized access in the application.
     * When a user attempts to access a secured resource without proper authentication, this entry point is triggered.
     * It sends a response with a 401 Unauthorized status and a message indicating access denial.
     *
     * @param request       The HTTP request.
     * @param response      The HTTP response.
     * @param authException The authentication exception that triggered the entry point.
     * @throws IOException      If an I/O exception occurs during response writing.
     * @throws ServletException If a servlet exception occurs.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Set HTTP status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Get PrintWriter for writing the response
        PrintWriter writer = response.getWriter();

        // Write a message indicating access denial
        writer.println("Access Denied !! " + authException.getMessage());
    }
}
