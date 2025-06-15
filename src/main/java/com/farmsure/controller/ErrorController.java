package com.farmsure.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An error occurred";
        String errorTitle = "Error";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle = "Page Not Found";
                errorMessage = "The page you're looking for doesn't exist.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle = "Access Denied";
                errorMessage = "You don't have permission to access this page.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorTitle = "Internal Server Error";
                errorMessage = "Something went wrong on our end. Please try again later.";
            }

            model.addAttribute("statusCode", statusCode);
        }

        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);

        return "error/error";
    }
}
