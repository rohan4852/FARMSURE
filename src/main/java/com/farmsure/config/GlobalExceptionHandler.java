package com.farmsure.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex, Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/403");
        mav.addObject("errorTitle", "Access Denied");
        mav.addObject("errorMessage", "You do not have permission to access this page.");
        return mav;
    }
}
