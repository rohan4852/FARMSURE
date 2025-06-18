// Theme management
document.addEventListener('DOMContentLoaded', function () {
    // Get saved theme or default to 'light'
    const savedTheme = localStorage.getItem('theme') || 'light';
    applyTheme(savedTheme);

    // Update all theme toggle buttons
    document.querySelectorAll('.theme-toggle').forEach(button => {
        updateThemeButton(button, savedTheme);
        button.addEventListener('click', toggleTheme);
    });
});

function applyTheme(theme) {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);

    // Update all theme toggle buttons
    document.querySelectorAll('.theme-toggle').forEach(button => {
        updateThemeButton(button, theme);
    });

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

function updateThemeButton(button, theme) {
    // Update icon and title based on current theme
    const icon = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    const title = theme === 'dark' ? 'Switch to light mode' : 'Switch to dark mode';
    button.innerHTML = `<i class="${icon}"></i>`;
    button.title = title;

    // Update aria-label for accessibility
    button.setAttribute('aria-label', title);
}
