package com.farmsure.model;

public class TypingStatus {
    private Long userId;
    private Long recipientId;
    private boolean isTyping;

    public TypingStatus() {
    }

    public TypingStatus(Long userId, Long recipientId, boolean isTyping) {
        this.userId = userId;
        this.recipientId = recipientId;
        this.isTyping = isTyping;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
