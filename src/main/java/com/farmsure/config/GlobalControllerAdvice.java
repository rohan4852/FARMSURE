package com.farmsure.config;

import com.farmsure.model.User;
import com.farmsure.service.MessageService;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @ModelAttribute("unreadMessages")
    public Integer unreadMessages() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            java.util.Optional<User> optionalUser = userService.findByUsername(auth.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                return messageService.getUnreadCountForUser(user);
            } else {
                return 0;
            }
        }
        return 0;
    }
}
