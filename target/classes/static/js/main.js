// Form validation
document.addEventListener('DOMContentLoaded', function () {
    // Enable Bootstrap form validation
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Password confirmation validation
    const passwordForm = document.querySelector('form[action*="password"]');
    if (passwordForm) {
        const newPassword = passwordForm.querySelector('#newPassword');
        const confirmPassword = passwordForm.querySelector('#confirmPassword');

        passwordForm.addEventListener('submit', event => {
            if (newPassword.value !== confirmPassword.value) {
                event.preventDefault();
                confirmPassword.setCustomValidity('Passwords do not match');
            } else {
                confirmPassword.setCustomValidity('');
            }
        });
    }

    // Profile image preview
    const imageInput = document.querySelector('input[type="file"][accept="image/*"]');
    const previewImage = document.querySelector('.profile-image');

    if (imageInput && previewImage) {
        imageInput.addEventListener('change', event => {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = e => previewImage.src = e.target.result;
                reader.readAsDataURL(file);
            }
        });
    }

    // Auto-scroll chat to bottom
    const chatContainer = document.querySelector('.chat-container');
    if (chatContainer) {
        chatContainer.scrollTop = chatContainer.scrollHeight;
    }

    // Enable tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
});

// Toast notifications
function showToast(message, type = 'success') {
    const toastContainer = document.getElementById('toast-container');
    if (!toastContainer) return;

    const toast = document.createElement('div');
    toast.classList.add('toast', 'show', `bg-${type}`, 'text-white');
    toast.setAttribute('role', 'alert');
    toast.setAttribute('aria-live', 'assertive');
    toast.setAttribute('aria-atomic', 'true');

    toast.innerHTML = `
        <div class="toast-header">
            <strong class="me-auto">Notification</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">${message}</div>
    `;

    toastContainer.appendChild(toast);
    setTimeout(() => toast.remove(), 5000);
}

// Message form validation
document.addEventListener('DOMContentLoaded', function () {
    const messageForm = document.querySelector('form[action="/messages"]');
    if (messageForm) {
        messageForm.addEventListener('submit', function (e) {
            const recipient = document.getElementById('recipient');
            const subject = document.getElementById('subject');
            const content = document.getElementById('content');

            if (!recipient.value || !subject.value || !content.value) {
                e.preventDefault();
                alert('Please fill in all fields');
            }
        });
    }

    // Handle message deletion
    const deleteButtons = document.querySelectorAll('button[type="submit"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            if (!confirm('Are you sure you want to delete this item?')) {
                e.preventDefault();
            }
        });
    });
});
