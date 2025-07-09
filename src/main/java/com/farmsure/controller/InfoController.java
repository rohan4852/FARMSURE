package com.farmsure.controller;

import com.farmsure.model.ContactMessage;
import com.farmsure.service.ContactMessageService;
import com.farmsure.service.OpenAIAutoReplyService;
import com.farmsure.service.GmailMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/info")
public class InfoController {
    @Autowired
    private ContactMessageService contactMessageService;
    @Autowired
    private OpenAIAutoReplyService openAIAutoReplyService;
    @Autowired
    private GmailMailService gmailMailService;

    @GetMapping("/about")
    public String about(Model model) {
        return "info/about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contactMessage", new ContactMessage());
        return "info/contact";
    }

    @PostMapping("/contact")
    public String submitContact(@ModelAttribute ContactMessage contactMessage, Model model) {
        contactMessageService.save(contactMessage);
        // Generate AI reply
        String aiReply = openAIAutoReplyService.generateReply(contactMessage.getName(), contactMessage.getMessage());
        // Send email to user
        try {
            gmailMailService.sendMail(contactMessage.getEmail(), "Thank you for contacting FarmSure!", aiReply);
            model.addAttribute("successMessage",
                    "Your message has been received. An automated reply has been sent to your email.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Message saved, but failed to send email reply.");
        }
        return "info/contact";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "info/privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        return "info/terms";
    }
}
