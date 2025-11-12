// ========================
// Funciones generales
// ========================

function showSection(sectionId) {
	const sections = document.querySelectorAll('.section');
	sections.forEach(section => section.classList.remove('active'));
	document.getElementById(sectionId).classList.add('active');
	window.scrollTo({ top: 0, behavior: 'smooth' });
}

// ========================
// Dropdown usuario
// ========================

function toggleUserDropdown() {
	const dropdown = document.getElementById('userDropdown');
	dropdown.classList.toggle('show');
}

document.addEventListener('click', function(event) {
	const dropdown = document.getElementById('userDropdown');
	const avatar = document.getElementById('userAvatar');
	if (avatar && dropdown && !avatar.contains(event.target) && !dropdown.contains(event.target)) {
		dropdown.classList.remove('show');
	}
});

// ========================
// Tema claro / oscuro
// ========================

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

// ========================
// Manejo de modales
// ========================

function openModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) {
		modal.style.display = 'flex';
		document.body.style.overflow = 'hidden';
	}
}

function closeModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) {
		modal.style.display = 'none';
		document.body.style.overflow = 'auto';
	}
}

window.addEventListener('click', (e) => {
	const modals = document.querySelectorAll('.modal');
	modals.forEach(modal => {
		if (e.target === modal) {
			closeModal(modal.id);
		}
	});
});

// ========================
// Reprogramar cita
// ========================

function openRescheduleModal(citaId) {
	document.getElementById('rescheduleIdModal').value = citaId;
	openModal('rescheduleModal');
	const today = new Date().toISOString().split('T')[0];
	document.getElementById('newAppointmentDate').min = today;
	document.getElementById('newAppointmentDate').value = today;
}

// ========================
// Crear Servicio
// ========================

function openServiceModal() {
	document.getElementById("serviceForm").reset();
	document.getElementById("serviceModalTitle").textContent = "Crear Servicio";
	document.getElementById("serviceForm").setAttribute("action", "/profesional/crear-servicio");
	document.getElementById("serviceIdModal").removeAttribute("name");
	document.getElementById("serviceModal").style.display = "flex";
}

// ========================
// Editar Servicio
// ========================

function editService(button) {
	// Obtener datos desde los atributos del botón
	const id = button.getAttribute("data-id");
	const nombre = button.getAttribute("data-nombre");
	const descripcion = button.getAttribute("data-descripcion");
	const precio = button.getAttribute("data-precio");
	const duracion = button.getAttribute("data-duracion");

	// Configurar modal para edición
	document.getElementById("serviceModalTitle").textContent = "Editar Servicio";
	document.getElementById("serviceForm").setAttribute("action", "/profesional/actualizar-servicio"); // ✅ CORREGIDO

	// Asignar valores al formulario
	document.getElementById("serviceIdModal").setAttribute("name", "id"); // ✅ necesario para enviar el ID
	document.getElementById("serviceIdModal").value = id;
	document.getElementById("serviceName").value = nombre;
	document.getElementById("serviceDescription").value = descripcion;
	document.getElementById("servicePrice").value = precio;
	document.getElementById("serviceDuration").value = duracion;

	// Mostrar modal
	document.getElementById("serviceModal").style.display = "flex";
}

// ========================
// Inicialización
// ========================

document.addEventListener('DOMContentLoaded', function() {
	initTheme();
});
