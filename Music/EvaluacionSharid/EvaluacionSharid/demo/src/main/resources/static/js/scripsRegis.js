// Selector de tipo de usuario
function selectUserType(type) {
	document.querySelectorAll('.user-type-btn').forEach(btn => {
		btn.classList.remove('active');
	});
	event.target.closest('.user-type-btn').classList.add('active');
	document.getElementById('userType').value = type;

	const professionalFields = document.getElementById('professionalFields');
	const especialidadInput = document.getElementById('especialidad');

	if (type === 'profesional') {
		professionalFields.style.display = 'block';
		especialidadInput.setAttribute('required', 'required');
		// Cambiar action del formulario
		document.getElementById('registerForm').action = '/registrar/profesional';
	} else {
		professionalFields.style.display = 'none';
		especialidadInput.removeAttribute('required');
		// Cambiar action del formulario
		document.getElementById('registerForm').action = '/registrar/usuario';
	}
}

// Habilitar/deshabilitar botón de registro según términos
document.getElementById('termsCheckbox').addEventListener('change', function() {
	document.getElementById('registerBtn').disabled = !this.checked;
});

// Validar contraseñas
document.getElementById('registerForm').addEventListener('submit', function(e) {
	const password = document.getElementById('password').value;
	const confirmPassword = document.getElementById('confirmPassword').value;

	if (password !== confirmPassword) {
		e.preventDefault();
		alert('❌ Las contraseñas no coinciden');
		return false;
	}

	if (password.length < 6) {
		e.preventDefault();
		alert('❌ La contraseña debe tener al menos 6 caracteres');
		return false;
	}
});

// Inicializar formulario
document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('registerForm').action = '/registrar/usuario';
});