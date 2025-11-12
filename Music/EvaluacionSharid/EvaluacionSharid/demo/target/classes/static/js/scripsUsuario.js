let currentSlide = 0;

function showSection(sectionId) {
	const sections = document.querySelectorAll('.section');
	sections.forEach(section => section.classList.remove('active'));
	document.getElementById(sectionId).classList.add('active');
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

function toggleUserDropdown() {
	const dropdown = document.getElementById('userDropdown');
	dropdown.classList.toggle('show');
}

document.addEventListener('click', function(event) {
	const dropdown = document.getElementById('userDropdown');
	const avatar = document.getElementById('userAvatar');
	if (!avatar.contains(event.target) && !dropdown.contains(event.target)) {
		dropdown.classList.remove('show');
	}
});

function toggleTheme() {
	const body = document.body;
	const themeIcon = document.getElementById('themeIcon');
	const currentTheme = body.getAttribute('data-theme');
	if (currentTheme === 'light') {
		body.setAttribute('data-theme', 'dark');
		themeIcon.className = 'fas fa-sun';
		localStorage.setItem('theme', 'dark');
	} else {
		body.setAttribute('data-theme', 'light');
		themeIcon.className = 'fas fa-moon';
		localStorage.setItem('theme', 'light');
	}
}

function initTheme() {
	const savedTheme = localStorage.getItem('theme');
	const themeIcon = document.getElementById('themeIcon');
	if (savedTheme === 'dark') {
		document.body.setAttribute('data-theme', 'dark');
		themeIcon.className = 'fas fa-sun';
	}
}

function nextSlide() {
	const items = document.querySelectorAll('.carousel-item');
	const dots = document.querySelectorAll('.carousel-dot');
	items[currentSlide].classList.remove('active');
	dots[currentSlide].classList.remove('active');
	currentSlide = (currentSlide + 1) % items.length;
	items[currentSlide].classList.add('active');
	dots[currentSlide].classList.add('active');
}

function prevSlide() {
	const items = document.querySelectorAll('.carousel-item');
	const dots = document.querySelectorAll('.carousel-dot');
	items[currentSlide].classList.remove('active');
	dots[currentSlide].classList.remove('active');
	currentSlide = (currentSlide - 1 + items.length) % items.length;
	items[currentSlide].classList.add('active');
	dots[currentSlide].classList.add('active');
}

function goToSlide(index) {
	const items = document.querySelectorAll('.carousel-item');
	const dots = document.querySelectorAll('.carousel-dot');
	items[currentSlide].classList.remove('active');
	dots[currentSlide].classList.remove('active');
	currentSlide = index;
	items[currentSlide].classList.add('active');
	dots[currentSlide].classList.add('active');
}

function mostrarDetallesServicio(servicioId) {
	if (!servicioId) {
		document.getElementById('detallesServicio').style.display = 'none';
		return;
	}

	const select = document.getElementById('servicioSelect');
	const option = select.options[select.selectedIndex];

	document.getElementById('profesionalNombre').textContent = option.getAttribute('data-profesional-nombre');
	document.getElementById('servicioDescripcion').textContent = option.getAttribute('data-descripcion');
	document.getElementById('servicioDuracion').textContent = option.getAttribute('data-duracion');
	document.getElementById('servicioPrecio').textContent = option.getAttribute('data-precio');
	document.getElementById('profesionalId').value = option.getAttribute('data-profesional-id');

	document.getElementById('detallesServicio').style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function() {
	initTheme();
	const today = new Date().toISOString().split('T')[0];
	document.getElementById('bookingDate').min = today;
	setInterval(nextSlide, 5000);
});