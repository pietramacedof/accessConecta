function sendToast(status, message) {
	var title;
	if (status == 'error') {
		$('#liveToast').addClass('danger');
		$('#liveToast').removeClass('success');
		title = "Erro!";
	}
	if (status == 'success') {
		$('#liveToast').addClass('success');
		$('#liveToast').removeClass('danger');
		title = "Sucesso!";
	}
	$('#message-toast').html(`<b> ${title}</b><br> ${message}`);
	$('#liveToast').toast('show');
};

function resetFields() {
	document.getElementById('firstName').value = "";
	document.getElementById('lastName').value = "";
	document.getElementById('email').value = "";
	document.getElementById('password').value = "";
	document.getElementById('age').value = "";
	document.getElementById('typeOfDisability').value = "";
}

function valideEmail(email) {
	if (!email) return;

	var regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	var teste = regexEmail.test(email);
	console.log(teste);

	return regexEmail.test(email);
}

function valideDate(dateString) {
	// Convertendo a string de data para um objeto Date
	var date = new Date(dateString);

	// Obtendo a data atual
	var currentDate = new Date();

	var isOlderAge = currentDate - date;

	// Convertendo a diferença para anos
	var age = Math.floor(isOlderAge / (365.25 * 24 * 60 * 60 * 1000));

	// Verificando se a pessoa tem 18 anos ou mais
	if (age >= 18) {
		return true; // A data é válida e a pessoa tem 18 anos ou mais
	} else {
		return false; // A data é válida, mas a pessoa tem menos de 18 anos
	}
}


document.getElementById('registerForm').addEventListener('submit', function(event) {
	event.preventDefault();

	var firstName = document.getElementById('firstName').value;
	var lastName = document.getElementById('lastName').value;
	var email = document.getElementById('email').value;
	var watchword = document.getElementById('password').value;
	var dateOfBirth = document.getElementById('age').value;
	var typeOfDisability = document.getElementById('typeOfDisability').value;

	var userType = !!document.querySelector('input[name="userType"]:checked') ? document.querySelector('input[name="userType"]:checked').value : null;

	var dateOrDesability = userType == 'owner' ? dateOfBirth : typeOfDisability;
	//queryString

	if (!firstName || !lastName || !email || !watchword || !userType || !dateOrDesability) {
		console.log('Campo vazio');

		sendToast("error", "Verifique se todos os campos estão preenchidos");
		$('.pageload').hide();

		return;
	}

	if (!valideEmail(email)) {
		sendToast("error", "Formato de e-mail inválido.");
		return;
	}

	// validação da data


	// Obtenha o valor do botão de rádio selecionado


	// Construa a URL da API com base no tipo de usuário
	var apiUrl = './cadastro';
	const data = {
		firstName: firstName,
		lastName: lastName,
		email: email,
		password: watchword,
		userType: userType
	};
	if (userType === 'evaluator') data['typeOfDisability'] = typeOfDisability;
	else data['dateOfBirth'] = dateOfBirth;
	const queryString = Object.keys(data).map(key => key + '=' + encodeURIComponent(data[key])).join('&');
	if (userType === 'owner') {

		if (!valideDate(dateOfBirth)) {
			console.log('data inválida');
			sendToast("error", "O proprietário deve ter mais de 18 anos.");
			return;
		}

		axios.post(apiUrl, queryString, {
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
		})
			.then((response) => {
				console.log(response.data);
				var data = response.data;
				if (data.status == 'error') {
					sendToast(data.status, data.message);
					return;
				}
				resetFields();
				sendToast(data.status, data.message);
				$('#registerModal').hide();
				// chamar função de limpar campo.

			})
			.catch((error) => {
				console.error(error);
				sendToast("error", "Dados inválidos, tente novamente!");
			}).finally(() => {
				$('.pageload').hide();
			});
	} else {
		axios.post(apiUrl, queryString, {
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
		})
			.then((response) => {
				console.log(response.data);
				var data = response.data;
				if (data.status == 'error') {
					sendToast(data.status, data.message);
					return;
				}
				resetFields();
				sendToast(data.status, data.message);
				$('#registerModal').hide();
			})
			.catch((error) => {
				console.error(error);
				sendToast("error", "Dados inválidos, tente novamente!");
			}).finally(() => {
				$('.pageload').hide();
			});
	}
});

document.getElementById('formLogin').addEventListener('submit', function(event) {
	event.preventDefault();

	console.log('Login dispared');
	$('.pageload').show();

	var email = document.getElementById('emailLogin').value;
	var watchword = document.getElementById('watchwordLogin').value;

	console.log(email);

	// Construa a URL da API com base no tipo de usuário
	var apiUrl = './login';

	const data = {
		email: email,
		password: watchword
	};

	if (!email || !watchword) {
		console.log('Campo vazio');

		sendToast("error", "Verifique se todos os campos estão preenchidos");
		$('.pageload').hide();

		return;
	}

	if (!valideEmail(email)) {
		sendToast("error", "Formato de e-mail inválido.");
		return;
	}

	const queryString = Object.keys(data).map(key => `${key}=${data[key]}`).join("&");

	axios.post(apiUrl, queryString, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
	})

		.then((response) => {
			console.log(response.data);
			var data = response.data;
			if (data.userType == 1) {
				localStorage.clear();
				localStorage.setItem('firstName', data.firstName);
				localStorage.setItem('lastName', data.lastName);
				localStorage.setItem('email', data.email);
				localStorage.setItem('dateOfBirth', data.dateOfBirth);
				localStorage.setItem('userType', data.userType);
				localStorage.setItem('token', data.token);
				window.location.href='./owner.html';
			}
			else {
				localStorage.clear();
				localStorage.setItem('firstName', data.firstName);
				localStorage.setItem('lastName', data.lastName);
				localStorage.setItem('email', data.email);
				localStorage.setItem('typeOfDisability', data.typeOfDisability);
				localStorage.setItem('userType', data.userType);
				localStorage.setItem('token', data.token);
			}

			resetFields();
			sendToast(data.status, data.message);
			$('#registerModal').hide();
		})
		.catch((error) => {
			console.error(error);
			sendToast("error", "Dados inválidos, tente novamente!");
		}).finally(() => {
			$('.pageload').hide();
		});
});





































