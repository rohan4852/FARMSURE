package com.farmsure.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String targetUrl = "/dashboard"; // default url

        if (role.equals("ROLE_FARMER")) {
            targetUrl = "/dashboard?type=farmer";
        } else if (role.equals("ROLE_MERCHANT")) {
            targetUrl = "/dashboard?type=merchant";
        } else if (role.equals("ROLE_ADMIN")) {
            targetUrl = "/admin/dashboard";
        }

        response.sendRedirect(request.getContextPath() + targetUrl);
    }
}
