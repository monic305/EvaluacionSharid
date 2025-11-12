// State Management
let currentAppointmentId = null;
let currentServiceId = null;
let isEditingService = false;

// Theme Toggle
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

// Check for saved theme preference
function initTheme() {
    const savedTheme = localStorage.getItem('theme');
    const themeIcon = document.getElementById('themeIcon');

    if (savedTheme === 'dark') {
        document.body.setAttribute('data-theme', 'dark');
        themeIcon.className = 'fas fa-sun';
    } else {
        document.body.removeAttribute('data-theme');
        themeIcon.className = 'fas fa-moon';
    }
}

// Navigation
function showSection(sectionId) {
    const sections = document.querySelectorAll('.section');
    sections.forEach(section => section.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// User Dropdown
function toggleUserDropdown() {
    const dropdown = document.getElementById('userDropdown');
    dropdown.classList.toggle('show');
}

// Close dropdown when clicking outside
document.addEventListener('click', function (event) {
    const dropdown = document.getElementById('userDropdown');
    const avatar = document.getElementById('userAvatar');

    if (!avatar.contains(event.target) && !dropdown.contains(event.target)) {
        dropdown.classList.remove('show');
    }
});

// Custom Alert Function
function showAlert(message, type = 'success') {
    const alert = document.getElementById('customAlert');
    const alertMessage = document.getElementById('alertMessage');
    const alertIcon = alert.querySelector('.alert-icon');

    alertMessage.textContent = message;
    alert.className = `custom-alert ${type} show`;

    // Change icon based on type
    if (type === 'success') {
        alertIcon.className = 'fas fa-check-circle alert-icon';
    } else if (type === 'error') {
        alertIcon.className = 'fas fa-exclamation-circle alert-icon';
    }

    setTimeout(() => {
        alert.classList.remove('show');
    }, 3000);
}

// Modal Functions
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'block';
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

// Close modal when clicking outside
window.addEventListener('click', (e) => {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (e.target === modal) {
            closeModal(modal.id);
        }
    });
});

// Load Appointments
function loadAppointments() {
    const appointments = [
        {
            id: 1,
            client: "María González",
            service: "Corte de Cabello",
            date: "2025-03-15",
            time: "10:00",
            status: "pending"
        },
        {
            id: 2,
            client: "Carlos Ruiz",
            service: "Coloración Completa",
            date: "2025-03-15",
            time: "11:30",
            status: "confirmed"
        },
        {
            id: 3,
            client: "Ana Martínez",
            service: "Tratamiento de Keratina",
            date: "2025-03-15",
            time: "14:00",
            status: "completed"
        },
        {
            id: 4,
            client: "Laura Díaz",
            service: "Corte y Peinado",
            date: "2025-03-16",
            time: "09:00",
            status: "pending"
        },
        {
            id: 5,
            client: "Sofía Ramírez",
            service: "Manicure y Pedicure",
            date: "2025-03-16",
            time: "11:00",
            status: "confirmed"
        }
    ];

    // Load today's appointments
    const todayContainer = document.getElementById('citasHoyList');
    const upcomingContainer = document.getElementById('citasProximasList');
    const professionalContainer = document.getElementById('professionalAppointments');

    todayContainer.innerHTML = '';
    upcomingContainer.innerHTML = '';
    professionalContainer.innerHTML = '';

    const today = new Date().toISOString().split('T')[0];

    appointments.forEach(appointment => {
        const appointmentElement = createAppointmentElement(appointment);

        if (appointment.date === today) {
            todayContainer.appendChild(appointmentElement);
        } else {
            upcomingContainer.appendChild(appointmentElement);
        }

        professionalContainer.appendChild(appointmentElement.cloneNode(true));
    });
}

// Create Appointment Element
function createAppointmentElement(appointment) {
    const div = document.createElement('div');
    div.className = 'appointment-item';

    let statusText = '';
    let statusColor = '';

    switch (appointment.status) {
        case 'pending':
            statusText = 'Pendiente';
            statusColor = '#f59e0b';
            break;
        case 'confirmed':
            statusText = 'Confirmada';
            statusColor = '#10b981';
            break;
        case 'completed':
            statusText = 'Completada';
            statusColor = '#6b7280';
            break;
    }

    div.innerHTML = `
                <h4>${appointment.client}</h4>
                <p><strong>Servicio:</strong> ${appointment.service}</p>
                <p><strong>Fecha:</strong> ${appointment.date}</p>
                <p><strong>Hora:</strong> ${appointment.time}</p>
                <p><strong>Estado:</strong> <span style="color: ${statusColor}">${statusText}</span></p>
                <div class="appointment-actions">
                    ${appointment.status === 'completed' ?
            '<button class="btn btn-success">Finalizada</button>' :
            '<button class="btn btn-danger" onclick="cancelAppointment(' + appointment.id + ')">Cancelar</button>'
        }
                    <button class="btn btn-warning" onclick="openRescheduleModal(' + appointment.id + ')">Reprogramar</button>
                </div>
            `;

    return div;
}

// Load Services
function loadServices() {
    const services = [
        {
            id: 1,
            name: "Corte de Cabello",
            description: "Corte profesional con asesoría personalizada según tu estilo y tipo de cabello",
            price: "$30.000",
            duration: "45 min"
        },
        {
            id: 2,
            name: "Coloración Completa",
            description: "Coloración completa con productos de alta calidad italiana",
            price: "$80.000",
            duration: "2 horas"
        },
        {
            id: 3,
            name: "Tratamiento de Keratina",
            description: "Tratamiento especializado para alisar y revitalizar el cabello",
            price: "$120.000",
            duration: "3 horas"
        }
    ];

    const container = document.getElementById('serviciosList');
    container.innerHTML = '';

    services.forEach(service => {
        const div = document.createElement('div');
        div.className = 'service-item';
        div.innerHTML = `
                    <div class="service-info">
                        <h4>${service.name}</h4>
                        <p>${service.description}</p>
                        <p><strong>Precio:</strong> ${service.price} | <strong>Duración:</strong> ${service.duration}</p>
                    </div>
                    <div class="service-actions">
                        <button class="btn btn-primary" onclick="editService(${service.id})">Editar</button>
                        <button class="btn btn-danger" onclick="deleteService(${service.id})">Eliminar</button>
                    </div>
                `;
        container.appendChild(div);
    });
}

// Appointment Management
function cancelAppointment(id) {
    if (confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        showAlert('Cita cancelada exitosamente', 'success');
        // In a real app, this would update the appointment status via API
    }
}

function openRescheduleModal(id) {
    currentAppointmentId = id;
    openModal('rescheduleModal');

    // Set minimum date to today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('newAppointmentDate').min = today;
    document.getElementById('newAppointmentDate').value = today;
}

function confirmReschedule() {
    const newDate = document.getElementById('newAppointmentDate').value;
    const newTime = document.getElementById('newAppointmentTime').value;
    const notes = document.getElementById('rescheduleNotes').value;

    if (!newDate || !newTime) {
        showAlert('Por favor completa todos los campos', 'error');
        return;
    }

    showAlert(`Cita reprogramada para el ${newDate} a las ${newTime}`, 'success');
    closeModal('rescheduleModal');

    // Reset form
    document.getElementById('rescheduleNotes').value = '';

    // In a real app, this would update the appointment via API
}

// Schedule Management
function editSchedule(day) {
    showAlert(`Editando horario para ${day}`, 'success');
}

function saveAvailability() {
    const day = document.getElementById('availabilityDay').value;
    const start = document.getElementById('startTime').value;
    const end = document.getElementById('endTime').value;
    const working = document.getElementById('workingDay').value;

    showAlert(`Disponibilidad guardada para ${day}: ${start} - ${end} (${working === 'yes' ? 'Laboral' : 'Cerrado'})`, 'success');
}

// Services Management
function openServiceModal(serviceId = null) {
    isEditingService = serviceId !== null;
    currentServiceId = serviceId;

    const modalTitle = document.getElementById('serviceModalTitle');
    modalTitle.textContent = isEditingService ? 'Editar Servicio' : 'Crear Servicio';

    if (isEditingService) {
        // In a real app, this would load the service data from an API
        document.getElementById('serviceName').value = 'Corte de Cabello';
        document.getElementById('serviceDescription').value = 'Corte profesional con asesoría personalizada';
        document.getElementById('servicePrice').value = '30000';
        document.getElementById('serviceDuration').value = '45';
    } else {
        // Clear form for new service
        document.getElementById('serviceName').value = '';
        document.getElementById('serviceDescription').value = '';
        document.getElementById('servicePrice').value = '';
        document.getElementById('serviceDuration').value = '';
    }

    openModal('serviceModal');
}

function editService(id) {
    openServiceModal(id);
}

function deleteService(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este servicio?')) {
        showAlert('Servicio eliminado exitosamente', 'success');
        // In a real app, this would delete the service via API
        loadServices(); // Reload services
    }
}

function saveService() {
    const name = document.getElementById('serviceName').value;
    const description = document.getElementById('serviceDescription').value;
    const price = document.getElementById('servicePrice').value;
    const duration = document.getElementById('serviceDuration').value;

    if (!name || !description || !price || !duration) {
        showAlert('Por favor completa todos los campos', 'error');
        return;
    }

    const action = isEditingService ? 'actualizado' : 'creado';
    showAlert(`Servicio ${action} exitosamente`, 'success');
    closeModal('serviceModal');

    // In a real app, this would save the service via API
    loadServices(); // Reload services
}

// Profile Management
function updateProfile() {
    const name = document.getElementById('profileNameInput').value;
    document.getElementById('profileName').textContent = name;
    document.getElementById('userAvatar').textContent = name.charAt(0).toUpperCase();
    showAlert('Perfil actualizado exitosamente', 'success');
}

// Logout
function logout() {
    if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
        showAlert('Sesión cerrada exitosamente', 'success');
        // In a real app, this would redirect to login page
        // window.location.href = '/logout';
    }
}

// Delete Account
function deleteAccount() {
    if (confirm('⚠️ ¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.')) {
        if (confirm('Esta es tu última oportunidad. ¿Realmente deseas eliminar tu cuenta?')) {
            showAlert('Tu cuenta ha sido eliminada', 'success');
            // In a real app, this would redirect to home page
        }
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', function () {
    // Initialize theme
    initTheme();

    // Load appointments
    loadAppointments();

    // Load services
    loadServices();
});