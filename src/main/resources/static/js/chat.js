/**
 * Enhanced WebSocket client for real-time chat messaging with file upload preview,
 * message status, typing indicator, and emoji picker integration.
 */

let stompClient = null;
let currentUserId = null;
let chatPartnerId = null;

function connect(userId, partnerId) {
    currentUserId = userId;
    chatPartnerId = partnerId;

    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Subscribe to personal message queue
        stompClient.subscribe('/user/' + currentUserId + '/queue/messages', function (messageOutput) {
            const message = JSON.parse(messageOutput.body);
            showMessage(message);
        });

        // Subscribe to typing indicator
        stompClient.subscribe('/user/' + currentUserId + '/queue/typing', function (typingOutput) {
            const typingData = JSON.parse(typingOutput.body);
            showTypingIndicator(typingData);
        });
    });
}

function sendMessage() {
    const contentInput = document.querySelector('textarea[name="content"]');
    const fileInput = document.getElementById('fileInput');

    if (!contentInput.value.trim() && (!fileInput.files || fileInput.files.length === 0)) {
        alert('Please enter a message or select a file.');
        return;
    }

    const message = {
        sender: { id: currentUserId },
        recipient: { id: chatPartnerId },
        content: contentInput.value.trim(),
        attachmentFileName: null,
        attachmentFileType: null,
        attachmentFileUrl: null,
        createdAt: new Date(),
        status: 'sending'
    };

    if (fileInput.files && fileInput.files.length > 0) {
        // Show file preview
        const file = fileInput.files[0];
        message.attachmentFileName = file.name;
        message.attachmentFileType = file.type;

        // Upload file via AJAX before sending message
        uploadFile(file).then(url => {
            message.attachmentFileUrl = url;
            sendMessageViaWebSocket(message);
        }).catch(err => {
            alert('File upload failed: ' + err);
        });
    } else {
        sendMessageViaWebSocket(message);
    }
}

function sendMessageViaWebSocket(message) {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
    clearInput();
    addMessageToUI(message);
}

function uploadFile(file) {
    return new Promise((resolve, reject) => {
        const formData = new FormData();
        formData.append('file', file);

        fetch('/messages/upload', {
            method: 'POST',
            body: formData
        }).then(response => {
            if (!response.ok) {
                reject('Upload failed with status ' + response.status);
            }
            return response.json();
        }).then(data => {
            resolve(data.fileUrl);
        }).catch(error => {
            reject(error);
        });
    });
}

function clearInput() {
    const contentInput = document.querySelector('textarea[name="content"]');
    const fileInput = document.getElementById('fileInput');
    contentInput.value = '';
    fileInput.value = '';
}

function addMessageToUI(message) {
    const chatMessages = document.getElementById('chatMessages');

    const messageWrapper = document.createElement('div');
    messageWrapper.classList.add(message.sender.id === currentUserId ? 'sent' : 'received');
    messageWrapper.setAttribute('data-message-id', message.id || '');

    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message-content');
    messageDiv.textContent = message.content;
    messageWrapper.appendChild(messageDiv);

    if (message.attachmentFileUrl) {
        if (message.attachmentFileType && message.attachmentFileType.startsWith('image/')) {
            const img = document.createElement('img');
            img.src = message.attachmentFileUrl;
            img.alt = message.attachmentFileName;
            img.classList.add('message-image');
            messageWrapper.appendChild(img);
        } else {
            const attachmentLink = document.createElement('a');
            attachmentLink.href = message.attachmentFileUrl;
            attachmentLink.target = '_blank';
            attachmentLink.textContent = message.attachmentFileName;
            messageWrapper.appendChild(attachmentLink);
        }
    }

    const timeDiv = document.createElement('div');
    timeDiv.classList.add('message-time');
    const time = new Date(message.createdAt);
    timeDiv.textContent = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    messageWrapper.appendChild(timeDiv);

    // Add message status indicator
    const statusDiv = document.createElement('div');
    statusDiv.classList.add('message-status');
    statusDiv.textContent = message.status ? message.status.charAt(0).toUpperCase() + message.status.slice(1) : '';
    messageWrapper.appendChild(statusDiv);

    chatMessages.appendChild(messageWrapper);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showMessage(message) {
    // Update message status or add new message
    const chatMessages = document.getElementById('chatMessages');
    const existingMessage = chatMessages.querySelector(`[data-message-id="${message.id}"]`);
    if (existingMessage) {
        // Update existing message content/status if needed
        existingMessage.querySelector('.message-content').textContent = message.content;
        const statusDiv = existingMessage.querySelector('.message-status');
        if (statusDiv) {
            statusDiv.textContent = message.status ? message.status.charAt(0).toUpperCase() + message.status.slice(1) : '';
        }
    } else {
        addMessageToUI(message);
    }
}

function showTypingIndicator(typingData) {
    let typingIndicator = document.getElementById('typingIndicator');
    if (!typingIndicator) {
        typingIndicator = document.createElement('div');
        typingIndicator.id = 'typingIndicator';
        typingIndicator.classList.add('typing-indicator');
        const chatContainer = document.querySelector('.chat-container');
        chatContainer.appendChild(typingIndicator);
    }

    if (typingData.isTyping && typingData.userId === chatPartnerId) {
        typingIndicator.style.display = 'block';
        typingIndicator.textContent = 'Typing...';
    } else {
        typingIndicator.style.display = 'none';
    }
}

function sendTypingStatus(isTyping) {
    if (!stompClient) return;
    const typingData = {
        userId: currentUserId,
        recipientId: chatPartnerId,
        isTyping: isTyping
    };
    stompClient.send("/app/chat.typing", {}, JSON.stringify(typingData));
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form.chat-input-form');
    const contentInput = document.querySelector('textarea[name="content"]');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        sendMessage();
    });

    contentInput.addEventListener('input', function () {
        sendTypingStatus(contentInput.value.length > 0);
    });

    // Initialize WebSocket connection with user IDs from template variables
    const currentUserId = parseInt(document.getElementById('currentUserId').value);
    const chatPartnerId = parseInt(document.getElementById('chatPartnerId').value);
    connect(currentUserId, chatPartnerId);
});
