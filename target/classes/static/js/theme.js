// Theme management
document.addEventListener('DOMContentLoaded', function () {
    // Get saved theme or default to 'light'
    const savedTheme = localStorage.getItem('theme') || 'light';
    applyTheme(savedTheme);

    // Create and append theme toggle button if it doesn't exist
    if (!document.querySelector('.theme-toggle')) {
        const button = document.createElement('button');
        button.className = 'theme-toggle';
        button.innerHTML = savedTheme === 'dark' ? '☀️' : '🌙';
        button.setAttribute('aria-label', 'Toggle theme');
        document.body.appendChild(button);

        // Add click event listener
        button.addEventListener('click', toggleTheme);
    }
});

function applyTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);

    // Update button icon if it exists
    const button = document.querySelector('.theme-toggle');
    if (button) {
        button.innerHTML = theme === 'dark' ? '☀️' : '🌙';
    }

    // Update meta theme-color for mobile browsers
    const metaThemeColor = document.querySelector('meta[name=theme-color]');
    if (metaThemeColor) {
        metaThemeColor.setAttribute('content', theme === 'dark' ? '#212529' : '#ffffff');
    }
}

function toggleTheme() {
    const currentTheme = localStorage.getItem('theme') || 'light';
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    applyTheme(newTheme);
}
