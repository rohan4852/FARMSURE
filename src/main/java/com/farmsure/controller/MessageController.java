package com.farmsure.controller;

import com.farmsure.model.Message;
import com.farmsure.model.User;
import com.farmsure.service.MessageService;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping("/inbox")
    public String inbox(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("messages", messageService.getReceivedMessages(user));
        return "dashboard/messages/inbox";
    }

    @GetMapping("/sent")
    public String sent(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("messages", messageService.getSentMessages(user));
        return "dashboard/messages/sent";
    }

    @GetMapping("/new")
    public String newMessage(Model model) {
        model.addAttribute("message", new Message());
        model.addAttribute("users", userService.getAllUsers());
        return "dashboard/messages/form";
    }

    @PostMapping
    public String sendMessage(@AuthenticationPrincipal User sender, @ModelAttribute Message message) {
        message.setSender(sender);
        messageService.sendMessage(message);
        return "redirect:/messages/sent";
    }

    @GetMapping("/{id}")
    public String viewMessage(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        Message message = messageService.getMessage(id);
        if (message.getRecipient().equals(user) && !message.isRead()) {
            messageService.markAsRead(message);
        }
        model.addAttribute("message", message);
        return "dashboard/messages/view";
    }

    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return "redirect:/messages/inbox";
    }
}
