$(document).ready(function () {

	$('a[href^="#"]').on('click', function (e) {
		e.preventDefault();
		var target = this.hash;
		var $target = $(target);

		if($target && $target.length > 0) {
    $('html, body').stop().delay(800).animate({
        'scrollTop': $target.offset().top
    }, 400, 'swing', function () {
        window.location.hash = target;
    });
}
	});
	$('#liveToastBtn').on('click', function (e) {
		e.preventDefault();
		console.log("click")
		$('#liveToast').toast('show');
	})
});

document.addEventListener('DOMContentLoaded', function () {console.log("loads")
	const lightModeIcon = document.getElementById('lightModeIcon');
	const darkModeIcon = document.getElementById('darkModeIcon');
	const sections = document.querySelectorAll('section');
	const logoDark = document.getElementById('logo-dark');
	const logoLight = document.getElementById('logo-light');
	const lightLogo = document.getElementById('light-logo');
	const darkLogo = document.getElementById('dark-logo');
	const registerModal = document.getElementById('registerModal');
	const registerLocationModal = document.getElementById('registerLocationModal');
	const loginModal = document.getElementById('loginModal');
	const logoLogin = document.getElementById('logo-login');
	const logoDarkLogin = document.getElementById('logo-login-dark');
	const headerOwner = document.getElementById('header-owner');
	const ownerHeader = document.getElementById('owner-header-d');
	const hasLocation = document.getElementById('hasLocation');
	const locationInfo = document.getElementById('locationInfo');
	lightModeIcon && lightModeIcon.addEventListener('click', function () {
		console.log('Light Mode Clicked');
		lightModeIcon.style.display = 'none';
		darkModeIcon.style.display = 'block';
		logoDark.style.display = 'block';
		!!logoLogin ? logoLogin.style.display = 'none' : '';
		!!logoDarkLogin ? logoDarkLogin.style.display = 'block' : '';
		logoLight.style.display = 'none';
		darkLogo.style.display = 'block';
		lightLogo.style.display = 'none';
		registerModal && registerModal.classList.remove('dark-mode');
		registerLocationModal && registerLocationModal.classList.remove('dark-mode');
		locationInfo && locationInfo.classList.remove('dark-mode');
		hasLocation && hasLocation.classList.remove('dark-mode');
		sections.forEach(function (section) {
			section.classList.remove('dark-mode');
		});
		loginModal && loginModal.classList.remove('dark-mode');
		headerOwner && headerOwner.classList.remove('dark-mode');
		ownerHeader & ownerHeader.classList.remove('dark-mode');
	});


	darkModeIcon && darkModeIcon.addEventListener('click', function () {
		console.log('Dark Mode Clicked');
		lightModeIcon.style.display = 'block';
		darkModeIcon.style.display = 'none';
		!!logoLogin ? logoLogin.style.display = 'block' : '';
		!!logoDarkLogin ? logoDarkLogin.style.display = 'none' : '';
		logoDark.style.display = 'none';
		logoLight.style.display = 'block';
		darkLogo.style.display = 'none';
		lightLogo.style.display = 'block';
		registerModal && registerModal.classList.add('dark-mode');
		registerLocationModal && registerLocationModal.classList.add('dark-mode');
		hasLocation && hasLocation.classList.add('dark-mode');
		locationInfo && locationInfo.classList.add('dark-mode');
		sections.forEach(function (section) {
			section.classList.add('dark-mode');
		});
		loginModal && loginModal.classList.add('dark-mode');
		headerOwner && headerOwner.classList.add('dark-mode');
		ownerHeader & ownerHeader.classList.add('dark-mode');
	});
});

document.addEventListener('DOMContentLoaded', function () {
	// Botões e modal
	const openModalBtn = document.getElementById('modalLogin');
	const loginModal = document.getElementById('loginModal');
	const close = document.getElementById('closeLogin');


	openModalBtn && openModalBtn.addEventListener('click', function () {
		loginModal.style.display = 'block';
	});

	close && close.addEventListener('click', function () {
		loginModal.style.display = 'none';
	});

	window.addEventListener('click', function (event) {
		if (event.target === loginModal) {
			loginModal.style.display = 'none';
		}
	});

	document.addEventListener('keydown', function (event) {
		if (event.key === 'Escape' && loginModal.style.display === 'block') {
			loginModal.style.display = 'none';
		}
	});

});

var signUp = document.getElementById('signUp');

signUp && signUp.addEventListener('click', function () {
	console.log('oi');


	// Close the login modal:
	const loginModal = document.getElementById('loginModal');
	loginModal.style.display = 'none';

	// Open the register modal (if applicable, using vanilla JavaScript):
	const registerModal = document.getElementById('registerModal');
	registerModal.style.display = 'block';  // Assuming you're using 'display: none' to hide it initially

});

var redirectLogin = document.getElementById('redirectLogin');

redirectLogin && redirectLogin.addEventListener('click', function () {
	console.log('oi');

	// Open the register modal (if applicable, using vanilla JavaScript):
	const registerModal = document.getElementById('registerModal');
	registerModal.style.display = 'none';  // Assuming you're using 'display: none' to hide it initially

	// Close the login modal:
	const loginModal = document.getElementById('loginModal');
	loginModal.style.display = 'block';
});


document.addEventListener('DOMContentLoaded', function () {
	// Botões e modal
	const openModalBtn = document.getElementById('openModalBtn');
	const closeModalBtn = document.getElementById('closeModalBtn');
	const registerModal = document.getElementById('registerModal');

	// Abrir o modal ao clicar no botão
	openModalBtn && openModalBtn.addEventListener('click', function () {
		registerModal.style.display = 'block';
	});

	// Fechar o modal ao clicar no botão de fechar
	closeModalBtn && closeModalBtn.addEventListener('click', function () {
		registerModal.style.display = 'none';
	});

	// Fechar o modal se clicar fora dele
	window.addEventListener('click', function (event) {
		if (event.target === registerModal) {
			registerModal.style.display = 'none';
		}
	});

	// Fechar o modal ao pressionar Esc
	document.addEventListener('keydown', function (event) {
		if (event.key === 'Escape' && registerModal.style.display === 'block') {
			registerModal.style.display = 'none';
		}
	});

	// Enviar dados do formulário (aqui você pode adicionar a lógica de envio)
	const cadastroForm = document.getElementById('cadastroForm');
	cadastroForm && cadastroForm.addEventListener('submit', function (event) {
		event.preventDefault();
		// Adicione a lógica de envio do formulário aqui
		console.log('Formulário enviado!');
		// Feche o modal após o envio
		registerModal.style.display = 'none';
	});
});

document.addEventListener('DOMContentLoaded', function () {
	var togglePassword = document.getElementById('togglePassword');
	togglePassword && togglePassword.addEventListener('click', function (e) {
		// Previne o comportamento padrão do ícone
		e.preventDefault();

		// Alterna o tipo do campo de senha
		var password = document.getElementById('password');
		var type = password.getAttribute('type') === 'password' ? 'text' : 'password';
		password.setAttribute('type', type);

		// Alterna a classe do ícone
		this.classList.toggle('fa-eye');
		this.classList.toggle('fa-eye-slash');
	});
});

document.addEventListener('DOMContentLoaded', function () {
	loginPassword = document.getElementById('loginPassword');
	loginPassword && loginPassword.addEventListener('click', function (e) {
		// Previne o comportamento padrão do ícone
		e.preventDefault();

		// Alterna o tipo do campo de senha
		var password = document.getElementById('watchwordLogin');
		var type = password.getAttribute('type') === 'password' ? 'text' : 'password';
		password.setAttribute('type', type);

		// Alterna a classe do ícone
		this.classList.toggle('fa-eye');
		this.classList.toggle('fa-eye-slash');
	});
});


document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('input[type=radio][name="userType"]').forEach(function (radio) {
		radio.addEventListener('change', function () {
			var ageInput = document.getElementById('age');
			var ageP = document.getElementById('p-age');

			if (radio.value === 'owner') {
				ageInput.style.display = 'block';
				ageP.style.display = 'block';
			} else {
				ageInput.style.display = 'none';
				ageP.style.display = 'none';
			}
		});
	});
});

document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('input[type=radio][name="userType"]').forEach(function (radio) {
		radio.addEventListener('change', function () {
			var ageInput = document.getElementById('typeOfDisability');

			if (radio.value === 'evaluator') {
				ageInput.style.display = 'block';
			} else {
				ageInput.style.display = 'none';
			}
		});
	});
});

document.addEventListener('DOMContentLoaded', function () {
	console.log('DOMContentLoaded event fired');

	const cadastrarLocalModal = document.getElementById('cadastrarLocalModal');
	cadastrarLocalModal && cadastrarLocalModal.addEventListener('click', function () {
		cadastrarLocalModal.style.display = 'block';
	});
});

document.addEventListener('DOMContentLoaded', function () {
	console.log('ABRIU ESSA PORCARIA');

	const cadastrarLocalModal = document.getElementById('registerLocationBtn');
	const modal = document.getElementById('registerLocationModal');
	const btnClose = document.getElementById('closeRegisterLocation');

	cadastrarLocalModal && cadastrarLocalModal.addEventListener('click', function () {
		modal.style.display = 'block';
	});

	btnClose && btnClose.addEventListener('click', function () {
		modal.style.display = 'none';
	});

	// Fechar o modal se clicar fora dele
	window.addEventListener('click', function (event) {
		if (event.target === modal) {
			modal.style.display = 'none';
		}
	});

	// Fechar o modal ao pressionar Esc
	document.addEventListener('keydown', function (event) {
		if (event.key === 'Escape' && modal.style.display === 'block') {
			modal.style.display = 'none';
		}
	});
});

document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('input[type=radio][name="typeOfEstablishment"]').forEach(function (radio) {
		radio.addEventListener('change', function () {
			var spanStartDate = document.getElementById('showEvent');

			if (radio.value === 'event') {
				spanStartDate.style.display = 'block';
			} else {
				spanStartDate.style.display = 'none';
			}
		});
	});
});

document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('input[type=radio][name="typeOfEstablishment"]').forEach(function (radio) {
		radio.addEventListener('change', function () {
			var typeOfCuisine = document.getElementById('showCalendar');

			if (radio.value === 'restaurant') {
				typeOfCuisine.style.display = 'block';
			} else {
				typeOfCuisine.style.display = 'none';
			}
		});
	});
});

document.addEventListener('DOMContentLoaded', function () {
	document.querySelectorAll('input[type=radio][name="typeOfEstablishment"]').forEach(function (radio) {
		radio.addEventListener('change', function () {
			var productType = document.getElementById('showStore');

			if (radio.value === 'store') {
				productType.style.display = 'block';
			} else {
				productType.style.display = 'none';
			}
		});
	});
});


$('input[type="radio"]').on('click', function () {
	$('#registerLocationModal').find('.modal-content').css({ width: 'auto', height: 'auto', 'max-height': '100%' });
});

$(document).ready(function () {
	$('#cep-input').on("blur", async function () {
		var cep = $('#cep-input').val();
		var cepData = await getCEPData(cep);
		console.log(cepData);
		if (!!cepData) {
			!!cepData.logradouro ? $('#logradouro').val(cepData.logradouro).attr('disabled', 'true') : $('#logradouro').val("");
			!!cepData.bairro ? $('#bairro').val(cepData.bairro).attr('disabled', 'true') : $('#bairro').val("");
			!!cepData.localidade ? $('#cidade').val(cepData.localidade).attr('disabled', 'true') : $('#cidade').val("");
			!!cepData.uf ? $('#estado').val(cepData.uf).attr('disabled', 'true') : $('#estado').val("");
			!!cepData.complemento ? $('#complemento').val(cepData.complemento).attr('disabled', 'true') : $('#complemento').val("");
		}
	});
});

$(document).ready(function () {
	$('#postalCode').on("blur", async function () {
		var cep = $('#postalCode').val();
		var cepData = await getCEPData(cep);
		console.log(cepData);
		if (!!cepData) {
			!!cepData.logradouro ? $('#publicPlace').val(cepData.logradouro).attr('disabled', 'true') : $('#publicPlace').val("");
			!!cepData.bairro ? $('#neighborhood').val(cepData.bairro).attr('disabled', 'true') : $('#neighborhood').val("");
			!!cepData.localidade ? $('#city').val(cepData.localidade).attr('disabled', 'true') : $('#city').val("");
			!!cepData.uf ? $('#state').val(cepData.uf).attr('disabled', 'true') : $('#state').val("");
		}
	});
});

var getCEPData = async function (cep) {
	console.log("iniciado");
	const data = await axios.get(`https://viacep.com.br/ws/${cep}/json/`)
		.then(function (response) {
			return response.data;
		})
		.catch(function (error) {
			return null;
		}).finally(function () { console.log("finalizado") });
	return data;
}