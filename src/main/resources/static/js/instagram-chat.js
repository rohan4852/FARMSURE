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
    const contentInput = document.getElementById('messageInput');
    if (!contentInput.value.trim()) {
        alert('Please enter a message.');
        return;
    }

    const message = {
        sender: { id: currentUserId },
        recipient: { id: chatPartnerId },
        content: contentInput.value.trim(),
        createdAt: new Date(),
        status: 'sending'
    };

    sendMessageViaWebSocket(message);
    clearInput();
    addMessageToUI(message);
}

function sendMessageViaWebSocket(message) {
    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
}

function clearInput() {
    const contentInput = document.getElementById('messageInput');
    contentInput.value = '';
}

function addMessageToUI(message) {
    const chatMessages = document.getElementById('chatMessages');

    const messageWrapper = document.createElement('div');
    messageWrapper.classList.add(message.sender.id === currentUserId ? 'user-message' : 'partner-message');
    messageWrapper.setAttribute('data-message-id', message.id || '');

    const messageDiv = document.createElement('div');
    messageDiv.classList.add('message-content');
    messageDiv.textContent = message.content;
    messageWrapper.appendChild(messageDiv);

    if (message.attachmentFileUrl) {
        const attachmentLink = document.createElement('a');
        attachmentLink.href = message.attachmentFileUrl;
        attachmentLink.target = '_blank';
        attachmentLink.textContent = message.attachmentFileName;
        messageWrapper.appendChild(attachmentLink);
    }

    const timeDiv = document.createElement('div');
    timeDiv.classList.add('message-time');
    const time = new Date(message.createdAt);
    timeDiv.textContent = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    messageWrapper.appendChild(timeDiv);

    chatMessages.appendChild(messageWrapper);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function showMessage(message) {
    const chatMessages = document.getElementById('chatMessages');
    const existingMessage = chatMessages.querySelector(`[data-message-id="${message.id}"]`);
    if (existingMessage) {
        existingMessage.querySelector('.message-content').textContent = message.content;
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
        const chatMessages = document.getElementById('chatMessages');
        chatMessages.appendChild(typingIndicator);
    }

    if (typingData.isTyping && typingData.userId === chatPartnerId) {
        typingIndicator.style.display = 'flex';
        typingIndicator.innerHTML = '<span></span><span></span><span></span>';
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

document.getElementById('sendButton').addEventListener('click', sendMessage);
document.getElementById('messageInput').addEventListener('input', function () {
    sendTypingStatus(this.value.length > 0);
});

document.addEventListener('DOMContentLoaded', function () {
    const currentUserIdVal = document.querySelector('[th\\:text="${currentUserId}"]')?.textContent || null;
    const chatPartnerIdVal = document.querySelector('[th\\:text="${chatPartnerId}"]')?.textContent || null;
    if (currentUserIdVal && chatPartnerIdVal) {
        connect(parseInt(currentUserIdVal), parseInt(chatPartnerIdVal));
    }
});
