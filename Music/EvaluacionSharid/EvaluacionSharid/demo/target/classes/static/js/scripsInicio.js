// Theme Toggle
function toggleTheme() {
    const body = document.body;
    const themeIcon = document.getElementById('themeIcon');
    const currentTheme = body.getAttribute('data-theme');

    if (currentTheme === 'light') {
        body.setAttribute('data-theme', 'dark');
        themeIcon.className = 'fas fa-sun';
        localStorage.setItem('theme', 'dark');
        showAlert('Modo oscuro activado 🌙', 'success');
    } else {
        body.setAttribute('data-theme', 'light');
        themeIcon.className = 'fas fa-moon';
        localStorage.setItem('theme', 'light');
        showAlert('Modo claro activado ☀️', 'success');
    }
}

// Load saved theme
window.addEventListener('load', () => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.body.setAttribute('data-theme', savedTheme);
    document.getElementById('themeIcon').className = savedTheme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    createBubbles();
});

// Scroll to Top
function scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

window.addEventListener('scroll', () => {
    const scrollTop = document.getElementById('scrollTop');
    if (window.pageYOffset > 300) {
        scrollTop.classList.add('show');
    } else {
        scrollTop.classList.remove('show');
    }
});

// Modal Functions
function openModal(modalId) {
    document.getElementById(modalId).classList.add('show');
    document.body.style.overflow = 'hidden';
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('show');
    document.body.style.overflow = 'auto';
}

// Close modal on outside click
window.onclick = function (event) {
    if (event.target.classList.contains('modal')) {
        event.target.classList.remove('show');
        document.body.style.overflow = 'auto';
    }
}

// User Type Selection
function selectUserType(type) {
    document.querySelectorAll('.user-type-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.closest('.user-type-btn').classList.add('active');
    document.getElementById('userType').value = type;
}

// Terms Checkbox Handler
document.getElementById('termsCheckbox')?.addEventListener('change', function () {
    document.getElementById('registerBtn').disabled = !this.checked;
});

// Handle Login
function handleLogin(event) {
    event.preventDefault();
    const email = document.getElementById('loginEmail').value;
    showAlert(`¡Bienvenido! Inicio de sesión exitoso 🎉`, 'success');
    closeModal('loginModal');
    event.target.reset();
}

// Handle Register
function handleRegister(event) {
    event.preventDefault();
    const password = document.getElementById('registerPassword').value;
    const confirmPassword = document.getElementById('registerConfirmPassword').value;

    if (password !== confirmPassword) {
        showAlert('Las contraseñas no coinciden ❌', 'error');
        return;
    }

    if (password.length < 8) {
        showAlert('La contraseña debe tener al menos 8 caracteres ❌', 'error');
        return;
    }

    const name = document.getElementById('registerName').value;
    const userType = document.getElementById('userType').value;
    const userTypeText = userType === 'client' ? 'Cliente' : 'Profesional';

    showAlert(`¡Registro exitoso! Bienvenido ${name} como ${userTypeText} 🎉`, 'success');
    closeModal('registerModal');
    event.target.reset();
    document.getElementById('termsCheckbox').checked = false;
    document.getElementById('registerBtn').disabled = true;
    document.querySelectorAll('.user-type-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelector('.user-type-btn').classList.add('active');
}

// Show/Hide Pages
function showPage(pageId) {
    const pages = document.querySelectorAll('.page');
    pages.forEach(page => {
        page.classList.remove('active');
    });

    const selectedPage = document.getElementById(pageId);
    if (selectedPage) {
        selectedPage.classList.add('active');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}

// Custom Alert Function
function showAlert(message, type = 'success') {
    const alert = document.getElementById('customAlert');
    const alertMessage = document.getElementById('alertMessage');
    const alertIcon = alert.querySelector('.alert-icon');

    alertMessage.textContent = message;
    alert.className = `custom-alert ${type} show`;

    if (type === 'success') {
        alertIcon.className = 'fas fa-check-circle alert-icon';
    } else if (type === 'error') {
        alertIcon.className = 'fas fa-exclamation-circle alert-icon';
    }

    setTimeout(() => {
        alert.classList.remove('show');
    }, 4000);
}

// Animate elements on scroll
const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = '1';
            entry.target.style.animation = 'fadeInUp 0.8s ease-out forwards';
        }
    });
}, observerOptions);

// Observe all cards
document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.service-card, .testimonial-card, .mission-card, .feature-card');
    cards.forEach(card => {
        card.style.opacity = '0';
        observer.observe(card);
    });
});

// Create animated bubbles
function createBubbles() {
    const animatedBg = document.getElementById('animatedBg');
    animatedBg.innerHTML = '';
    for (let i = 0; i < 15; i++) {
        const bubble = document.createElement('div');
        bubble.className = 'bubble';
        bubble.style.left = Math.random() * 100 + '%';
        bubble.style.animationDelay = Math.random() * 5 + 's';
        bubble.style.animationDuration = (10 + Math.random() * 10) + 's';
        const size = (Math.random() * 40 + 20) + 'px';
        bubble.style.width = size;
        bubble.style.height = size;
        animatedBg.appendChild(bubble);
    }
}

// Service card click effect
document.addEventListener('click', (e) => {
    if (e.target.closest('.service-card')) {
        const card = e.target.closest('.service-card');
        const serviceName = card.querySelector('h3').textContent;
        const servicePrice = card.querySelector('.service-price').textContent;
        showAlert(`${serviceName} - ${servicePrice}. ¡Agenda tu cita! 💖`, 'success');
    }
});

// Add parallax effect to hero section
window.addEventListener('scroll', () => {
    const hero = document.querySelector('.hero');
    if (hero && document.getElementById('home').classList.contains('active')) {
        const scrolled = window.pageYOffset;
        hero.style.transform = `translateY(${scrolled * 0.3}px)`;
        hero.style.opacity = Math.max(1 - (scrolled * 0.002), 0.3);
    }
});


// Testimonial card hover effect
document.querySelectorAll('.testimonial-card').forEach(card => {
    card.addEventListener('mouseenter', function () {
        this.style.transform = 'translateY(-15px) scale(1.02)';
    });
    card.addEventListener('mouseleave', function () {
        this.style.transform = 'translateY(0) scale(1)';
    });
});

// Gallery item click
document.querySelectorAll('.gallery-item').forEach(item => {
    item.addEventListener('click', function () {
        showAlert('¡Próximamente más fotos de nuestro salón! 📸', 'success');
    });
});

// Smooth scroll for all anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});


// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    // ESC to close modals
    if (e.key === 'Escape') {
        document.querySelectorAll('.modal.show').forEach(modal => {
            modal.classList.remove('show');
            document.body.style.overflow = 'auto';
        });
    }
});

// Add sparkle effect on service images
setInterval(() => {
    const images = document.querySelectorAll('.service-image');
    images.forEach(img => {
        if (Math.random() > 0.8) {
            img.style.animation = 'none';
            setTimeout(() => {
                img.style.animation = '';
            }, 10);
        }
    });
}, 3000);

