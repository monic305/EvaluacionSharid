  // State Management
        let currentSlide = 0;
        let selectedProfessional = null;
        let selectedService = null;
        let selectedDate = null;
        let selectedTime = null;
        let currentRating = 0;
        let carouselInterval;

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

        // Carousel Functions
        function goToSlide(index) {
            const items = document.querySelectorAll('.carousel-item');
            const dots = document.querySelectorAll('.carousel-dot');
            const inner = document.querySelector('.carousel-inner');

            items[currentSlide].classList.remove('active');
            dots[currentSlide].classList.remove('active');

            currentSlide = index;

            items[currentSlide].classList.add('active');
            dots[currentSlide].classList.add('active');
            inner.style.transform = `translateX(-${currentSlide * 100}%)`;
        }

        function nextSlide() {
            const nextSlide = (currentSlide + 1) % 3;
            goToSlide(nextSlide);
            resetCarouselInterval();
        }

        function prevSlide() {
            const prevSlide = (currentSlide - 1 + 3) % 3;
            goToSlide(prevSlide);
            resetCarouselInterval();
        }

        function resetCarouselInterval() {
            clearInterval(carouselInterval);
            carouselInterval = setInterval(nextSlide, 5000);
        }

        // Start auto carousel
        carouselInterval = setInterval(nextSlide, 5000);

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

        // Load Services
        function loadServices() {
            const services = [
                {
                    id: 1,
                    name: "Corte de Cabello",
                    price: "$30.000",
                    description: "Corte profesional con asesoría personalizada según tu estilo y tipo de cabello. Incluye lavado y secado.",
                    image: "💇‍♀️",
                    featured: true
                },
                {
                    id: 2,
                    name: "Tinte y Coloración",
                    price: "$80.000",
                    description: "Coloración completa con productos de alta calidad italiana. Incluye tratamiento de protección capilar.",
                    image: "🎨",
                    featured: true
                },
                {
                    id: 3,
                    name: "Tratamientos Capilares",
                    price: "$60.000",
                    description: "Hidratación profunda, keratina, botox capilar y tratamientos especializados para revitalizar tu cabello.",
                    image: "💆‍♀️",
                    featured: true
                },
                {
                    id: 4,
                    name: "Manicure y Pedicure",
                    price: "$45.000",
                    description: "Cuidado completo de manos y pies con esmaltado gel de larga duración. Incluye masaje relajante.",
                    image: "💅",
                    featured: false
                },
                {
                    id: 5,
                    name: "Peinados para Eventos",
                    price: "$120.000",
                    description: "Peinados elegantes para bodas, graduaciones y eventos especiales. Incluye prueba previa.",
                    image: "👰",
                    featured: false
                },
                {
                    id: 6,
                    name: "Tratamientos Faciales",
                    price: "$70.000",
                    description: "Limpieza facial profunda, mascarillas personalizadas y cuidado integral de la piel con productos premium.",
                    image: "🧖‍♀️",
                    featured: false
                },
                {
                    id: 7,
                    name: "Depilación",
                    price: "$35.000",
                    description: "Depilación con cera de alta calidad en diferentes zonas. Productos hipoalergénicos disponibles.",
                    image: "✂️",
                    featured: false
                },
                {
                    id: 8,
                    name: "Maquillaje Profesional",
                    price: "$90.000",
                    description: "Maquillaje profesional para cualquier ocasión con productos MAC, Sephora y marcas premium.",
                    image: "💄",
                    featured: false
                },
                {
                    id: 9,
                    name: "Diseño de Cejas",
                    price: "$25.000",
                    description: "Perfilado y diseño de cejas con técnicas de microblading y laminado. Resultados naturales garantizados.",
                    image: "👁️",
                    featured: false
                },
                {
                    id: 10,
                    name: "Extensiones de Cabello",
                    price: "$250.000",
                    description: "Extensiones de cabello natural con técnicas de keratina. Aumenta volumen y largo instantáneamente.",
                    image: "🌟",
                    featured: false
                },
                {
                    id: 11,
                    name: "Maquillaje Permanente",
                    price: "$180.000",
                    description: "Micropigmentación de cejas, labios y delineado de ojos. Resultados duraderos y naturales.",
                    image: "💋",
                    featured: false
                },
                {
                    id: 12,
                    name: "Alisados y Permanentes",
                    price: "$150.000",
                    description: "Alisado brasileño, japonés y permanentes con productos de última generación. Duración hasta 6 meses.",
                    image: "🧴",
                    featured: false
                }
            ];

            // Load featured services
            const featuredContainer = document.getElementById('featuredServices');
            const allContainer = document.getElementById('allServices');
            const serviceSelect = document.getElementById('serviceSelect');

            // Clear containers
            featuredContainer.innerHTML = '';
            allContainer.innerHTML = '';
            serviceSelect.innerHTML = '<option value="">-- Elige un servicio --</option>';

            // Add services to containers
            services.forEach(service => {
                const serviceCard = createServiceCard(service);

                if (service.featured) {
                    featuredContainer.appendChild(serviceCard);
                }

                allContainer.appendChild(serviceCard.cloneNode(true));

                // Add to service select
                const option = document.createElement('option');
                option.value = service.id;
                option.textContent = service.name;
                serviceSelect.appendChild(option);
            });
        }

        // Create Service Card
        function createServiceCard(service) {
            const card = document.createElement('div');
            card.className = 'service-card';
            card.innerHTML = `
                <div class="service-image">${service.image}</div>
                <h3>${service.name}</h3>
                <div class="service-price">${service.price}</div>
                <p>${service.description}</p>
                <button class="rate-btn" onclick="openRatingModal(${service.id})">Calificar</button>
            `;

            // Add click event to select service
            card.addEventListener('click', function (e) {
                if (!e.target.classList.contains('rate-btn')) {
                    document.getElementById('serviceSelect').value = service.id;
                    loadProfessionals();
                    showSection('booking');
                }
            });

            return card;
        }

        // Load Professionals
        function loadProfessionals() {
            const serviceId = document.getElementById('serviceSelect').value;

            if (!serviceId) {
                document.getElementById('professionalsContainer').style.display = 'none';
                document.getElementById('bookingFormContainer').style.display = 'none';
                return;
            }

            // In a real app, this would be an API call
            const professionals = [
                { id: 1, name: 'Ana Martínez', rating: '4.9', experience: '8 años de experiencia', price: '$80.000' },
                { id: 2, name: 'Carlos Ruiz', rating: '4.8', experience: '5 años de experiencia', price: '$75.000' },
                { id: 3, name: 'Laura Gómez', rating: '4.7', experience: '10 años de experiencia', price: '$90.000' }
            ];

            const container = document.getElementById('professionalList');
            container.innerHTML = '';

            professionals.forEach(prof => {
                const div = document.createElement('div');
                div.className = 'professional-item';
                div.innerHTML = `
                    <div class="professional-info">
                        <h4>${prof.name}</h4>
                        <p>${prof.experience}</p>
                        <p class="rating">⭐ ${prof.rating} - ${prof.price}/sesión</p>
                    </div>
                    <button class="btn btn-primary" style="width:auto; padding:0.7rem 1.5rem;" onclick="selectProfessional(${prof.id}, '${prof.name}', ${serviceId})">
                        Seleccionar
                    </button>
                `;
                container.appendChild(div);
            });

            document.getElementById('professionalsContainer').style.display = 'block';
            document.getElementById('bookingFormContainer').style.display = 'none';
        }

        // Select Professional
        function selectProfessional(profId, name, serviceId) {
            selectedProfessional = { id: profId, name: name };
            selectedService = serviceId;

            document.getElementById('bookingFormContainer').style.display = 'block';

            // Set minimum date to today
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('bookingDate').min = today;
            document.getElementById('bookingDate').value = today;

            // Load time slots for today
            loadTimeSlots(today);

            // Scroll to booking form
            document.getElementById('bookingFormContainer').scrollIntoView({ behavior: 'smooth' });
        }

        // Load Time Slots
        function loadTimeSlots(date) {
            selectedDate = date;

            // In a real app, this would be an API call to get available slots
            const times = ['9:00', '10:00', '11:00', '12:00', '14:00', '15:00', '16:00', '17:00'];
            const container = document.getElementById('timeSlots');
            container.innerHTML = '';

            times.forEach(time => {
                const slot = document.createElement('div');
                slot.className = 'time-slot';
                slot.textContent = time;
                slot.onclick = function () {
                    document.querySelectorAll('.time-slot').forEach(s => s.classList.remove('selected'));
                    this.classList.add('selected');
                    selectedTime = this.textContent;
                };
                container.appendChild(slot);
            });
        }

        // Confirm Booking
        function confirmBooking() {
            if (!selectedDate) {
                showAlert('Por favor selecciona una fecha', 'error');
                return;
            }

            if (!selectedTime) {
                showAlert('Por favor selecciona un horario', 'error');
                return;
            }

            // In a real app, this would be an API call to save the appointment
            showAlert(`¡Tu cita ha sido agendada con ${selectedProfessional.name} para el ${selectedDate} a las ${selectedTime}!`, 'success');

            // Reset form
            document.getElementById('bookingFormContainer').style.display = 'none';
            document.getElementById('serviceSelect').value = '';
            document.getElementById('professionalsContainer').style.display = 'none';

            // Reset selections
            selectedProfessional = null;
            selectedService = null;
            selectedDate = null;
            selectedTime = null;

            // Show profile section to see the new appointment
            showSection('profile');

            // Reload appointments
            loadUserAppointments();
        }

        // Load User Appointments
        function loadUserAppointments() {
            // In a real app, this would be an API call
            const appointments = [
                {
                    id: 1,
                    service: "Corte de Cabello",
                    professional: "Ana Martínez",
                    date: "2025-03-15",
                    time: "10:00",
                    status: "confirmed"
                },
                {
                    id: 2,
                    service: "Tinte y Coloración",
                    professional: "Carlos Ruiz",
                    date: "2025-03-20",
                    time: "14:00",
                    status: "pending"
                },
                {
                    id: 3,
                    service: "Manicure y Pedicure",
                    professional: "Laura Gómez",
                    date: "2025-03-10",
                    time: "11:00",
                    status: "completed"
                }
            ];

            const container = document.getElementById('userAppointments');
            const noCitasMessage = document.getElementById('noCitasMessage');

            if (appointments.length === 0) {
                noCitasMessage.textContent = "No tienes citas programadas";
                return;
            }

            noCitasMessage.style.display = 'none';
            container.innerHTML = '';

            appointments.forEach(appointment => {
                const div = document.createElement('div');
                div.className = 'appointment-item';

                let statusText = '';
                let statusClass = '';

                switch (appointment.status) {
                    case 'pending':
                        statusText = 'Pendiente';
                        statusClass = 'status-pending';
                        break;
                    case 'confirmed':
                        statusText = 'Confirmada';
                        statusClass = 'status-confirmed';
                        break;
                    case 'completed':
                        statusText = 'Completada';
                        statusClass = 'status-completed';
                        break;
                }

                div.innerHTML = `
                    <h4>${appointment.service}</h4>
                    <p><strong>Profesional:</strong> ${appointment.professional}</p>
                    <p><strong>Fecha:</strong> ${appointment.date}</p>
                    <p><strong>Hora:</strong> ${appointment.time}</p>
                    <span class="appointment-status ${statusClass}">${statusText}</span>
                `;
                container.appendChild(div);
            });
        }

        // Rating Modal
        function openRatingModal(serviceId) {
            currentRating = 0;
            updateStars();
            openModal('ratingModal');
        }

        // Update Stars
        function updateStars() {
            const stars = document.querySelectorAll('.star');
            stars.forEach(star => {
                const rating = parseInt(star.getAttribute('data-rating'));
                if (rating <= currentRating) {
                    star.classList.add('active');
                } else {
                    star.classList.remove('active');
                }
            });
        }

        // Submit Rating
        function submitRating() {
            if (currentRating === 0) {
                showAlert('Por favor selecciona una calificación', 'error');
                return;
            }

            const comment = document.getElementById('ratingComment').value;

            // In a real app, this would be an API call to save the rating
            showAlert(`¡Gracias por tu calificación de ${currentRating} estrellas!`, 'success');

            closeModal('ratingModal');

            // Reset form
            document.getElementById('ratingComment').value = '';
        }

        // Logout
        function logout() {
            if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
                // In a real app, this would redirect to login page
                showAlert('Sesión cerrada exitosamente', 'success');
                // window.location.href = '/logout';
            }
        }

        // Initialize
        document.addEventListener('DOMContentLoaded', function () {
            // Initialize theme
            initTheme();

            // Load services
            loadServices();

            // Load user appointments
            loadUserAppointments();

            // Set up star rating
            const starsContainer = document.getElementById('starsContainer');
            starsContainer.addEventListener('click', function (e) {
                if (e.target.classList.contains('star')) {
                    currentRating = parseInt(e.target.getAttribute('data-rating'));
                    updateStars();
                }
            });

            // Set up date change listener
            document.getElementById('bookingDate').addEventListener('change', function (e) {
                loadTimeSlots(e.target.value);
            });
        });