package com.farmsure.controller;

import com.farmsure.model.Message;
import com.farmsure.model.User;
import com.farmsure.service.MessageService;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/inbox")
    public String inbox(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("messages", messageService.getReceivedMessages(user));
        return "dashboard/messages/inbox";
    }

    @PostMapping("/upload")
    @ResponseBody
    public java.util.Map<String, String> uploadFile(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            String uploadsDir = "uploads/";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadsDir);
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            String originalFilename = file.getOriginalFilename();
            String filePath = uploadsDir + System.currentTimeMillis() + "_" + originalFilename;
            java.nio.file.Path fileLocation = java.nio.file.Paths.get(filePath);
            file.transferTo(fileLocation.toFile());

            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("fileUrl", "/" + filePath);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed");
        }
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
    public String sendMessage(@AuthenticationPrincipal User sender, @ModelAttribute Message message,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            message.setSender(sender);
            message.setStatus("sent");
            if (file != null && !file.isEmpty()) {
                // Save file to local storage (e.g., "uploads" directory)
                String uploadsDir = "uploads/";
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadsDir);
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }
                String originalFilename = file.getOriginalFilename();
                String filePath = uploadsDir + System.currentTimeMillis() + "_" + originalFilename;
                java.nio.file.Path fileLocation = java.nio.file.Paths.get(filePath);
                file.transferTo(fileLocation.toFile());

                // Set attachment info in message
                message.setAttachmentFileName(originalFilename);
                message.setAttachmentFileType(file.getContentType());
                message.setAttachmentFileUrl("/" + filePath);
            }
            messageService.sendMessage(message);
            return "redirect:/messages/sent";
        } catch (Exception e) {
            e.printStackTrace();
            return "error/error";
        }
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

    @GetMapping("/chat")
    public String chat(@AuthenticationPrincipal User currentUser,
            @RequestParam("with") Long chatPartnerId,
            Model model) {
        User chatPartner = userService.findById(chatPartnerId);
        if (chatPartner == null) {
            return "error/404";
        }
        // Fetch messages between currentUser and chatPartner
        java.util.List<com.farmsure.model.Message> messages = messageService.getConversation(currentUser, chatPartner);
        model.addAttribute("messages", messages);
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("chatPartnerId", chatPartner.getId());
        model.addAttribute("chatPartnerName",
                chatPartner.getFullName() != null ? chatPartner.getFullName() : chatPartner.getUsername());
        return "dashboard/messages/chat";
    }

}
