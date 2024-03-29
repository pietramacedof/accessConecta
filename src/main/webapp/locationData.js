

$(document).ready(function () {

	const isLogged = localStorage.getItem('token');

	if (!isLogged) {
		localStorage.clear();
		window.location.href = './';
		console.log('tá logado', isLogged);
		return;
	}
	$('.pageload').show();
	axios.get('./isLogged', {
		headers: {
			Authorization: `Bearer ${isLogged}`
		}
	}).then((response) => {
		if (response.data.status != 'success') {
			localStorage.clear();
			window.location.href = './';
		}
		else {
			if (response.data.userType == 1) {
				console.log('Tipo de usuario: ', response.data.userType);
				checkUserHasLocation(isLogged);
			}
			else {
				console.log('Tipo de usuario: ', response.data.userType);
				loadLocations(isLogged);
			}


		}
	}).catch((error) => {
		console.error(error);
	}).finally(() => {
		$('.pageload').hide();
	});

	$('#filterEverthing').click(function () {
		console.log('show');
		$('.location-info').show();
	});

	$('a[href^="#"]').on('click', function (e) {
		e.preventDefault();
		var target = this.hash;
		var $target = $(target);

		if ($target && $target.length > 0) {
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
	});

	$(".locations").on("click", function () {
		$(".locations").blur();
	});

	$('#closeSession').click(function () {
		console.log('caiu');
		localStorage.clear();
		window.location.href = './';
	});

	$(function () {
		$('[data-toggle="tooltip"]').tooltip()
	});

	$('#radeButton').on('click', function () {
		var v = $('#idLocationHidden').val();

		var questionList = (localStorage.getItem('questionList'));
		// Converte a string em um array de números
		var questions = questionList.split(',').map(Number);

		// Usa a função map para adicionar "answer-" a cada elemento
		var mappedList = questions.map(function (question) {
			return 'answer-' + question;
		});

		var note = 0;
		console.log('Mapped List:', mappedList);

		for (var i = 0; i < mappedList.length; i++) {
			var mappedValue = mappedList[i];
			var selectedValue = $('input[name="' + mappedValue + '"]:checked').val();
			console.log(mappedValue + ':', selectedValue);
			if (selectedValue === undefined) {
				sendToast('error', 'Verifique se todos os campos estão preenchidos.');
				break;
			}
			selectedValue = parseFloat(selectedValue.replace(',', '.')); // Converter para número
			note += selectedValue;
		}
		console.log(note);
		var apiUrl = './hasLocation';
		const token = localStorage.getItem('token');
		const data = {
			note: note,
			id: v,
			token: token
		};
		var queryString = Object.keys(data).map(key => key + '=' + encodeURIComponent(data[key])).join('&');

		axios.post(apiUrl, queryString)
			.then(response => {
				console.log(response.data);
				var n = response.data.accessibilityNote;

				var arrayLocalStorage = JSON.parse(localStorage.getItem('locations'));
				console.log(v);

				var index = arrayLocalStorage.findIndex(obj => obj.id == v);
				console.log(index);

				console.log(array);

				arrayLocalStorage[index].accessibilityNote = n;
				var newQuantity = parseInt(arrayLocalStorage[index].quantityOfEvaluation) + 1;
				arrayLocalStorage[index].quantityOfEvaluation = newQuantity; // Supondo que você está incrementando o total de avaliações
				var array = arrayLocalStorage[index];
				var address = `${array.publicPlace}, ${array.number}, ${array.neighborhood}, ${array.city}, ${array.uf}`;
				console.log(array);
				console.log(array.quantityOfEvaluation);
				const specificStarRating = $(`.my-rating-${v}`); // Seleciona por ID
				specificStarRating.starRating('setRating', n);
				$(`#pMyRating-${v}`).html(`(${newQuantity})`);
				sendToast(response.data.status, response.data.message);// Aguarda  2 segundos antes de executar a função

				const buttonEvaluate = document.getElementById(`buttonEvaluate${v}`);

				buttonEvaluate.disabled = true;

				customCloseModal('evaluateData');
			})
			.catch(error => {
				console.error(`Erro ao enviar o número: ${error}`);
			});
	});

	$('body').on('click', '#learnMoreBtn', function () {
		console.log('entrou');
		var button = $(event.relatedTarget);
		var id = $(this).data('id'); 
		$('#learnMore').modal('show');
		var locationString = localStorage.getItem('locations');
		var locationsArray = JSON.parse(locationString);
		var locationObject;
		// Encontra o objeto Location com o ID específico
		locationObject = locationsArray.find(function(location) {
		return location.id == id;
		});
		var address = `${locationObject.publicPlace}, ${locationObject.number}, ${locationObject.neighborhood}, ${locationObject.city}, ${locationObject.uf}`;
		console.log(locationObject);
		var individualNote = locationObject.locationEvaluation == 'false' ? "Você não avaliou esse local ainda" : locationObject.noteEvaluation;
		var p = locationObject.locationEvaluation == 'false' ? "" : "Sua nota para este estabelecimento";
		 	
		 	if (locationObject.type == 'restaurant') {
		console.log(locationObject.id);
		template =
			`
			<div id='more' class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Restaurante ${locationObject.placeName}</h5>
					<button stype="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
						<div class="learn-more-stars">
							<div class="star-size">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="big" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
							</div>
							<div class="content-quantity">
								<p>${locationObject.acessibilityNote}</p>
								<p>(${locationObject.quantityOfEvaluation})</p>
							</div>
						</div>
						<div class="learn-more-content">
						<p><strong>Endereço: </strong>${address}</p>
						<p><strong>Dias de funcionamento: </strong>${locationObject.operatingDays}</p>
						<p><strong>Tipo de Culinária: </strong>${locationObject.typeOfCuisine}</p>
						<p><strong>${p} </strong>${individualNote}</p>
						</div>
				</div>
				<div class="modal-footer">
				</div>
				</div>`
	} else if (locationObject.type == 'event') {
		var start = formateDate(locationObject.startDate);
		var end = formateDate(locationObject.endDate);
		console.log(locationObject.id);
		var price = '';
		if (locationObject.eventPrice == '2') {
			price = "Pago";
		} else {
			price = 'Gratuito';
		}
		template =
			`
			<div id='more' class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Evento ${locationObject.placeName}</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
						<div class="learn-more-stars">
							<div class="star-size">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="big" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
							</div>
							<div class="content-quantity">
								<p>${locationObject.acessibilityNote}</p>
								<p>(${locationObject.quantityOfEvaluation})</p>
							</div>
						</div>
						<div class="learn-more-content">
						<p><strong>Endereço: </strong>${address}</p>
						<p><strong>Data de Início: </strong>${start}</p>
						<div class="badge-date">
							<p><strong>Data de Fim: </strong>${end}</p>
							<span class="badge badge-primary">${price}</span>
						</div>
						<p><strong>${p} </strong>${individualNote}</p>
						
						</div>
				</div>
				<div class="modal-footer">
				</div>
			</div>`
	} else {

		template =
			`
			<div id='more' class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Loja ${locationObject.placeName}</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
						<div class="learn-more-stars">
							<div class="star-size">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="big" src="assets/285661_star_icon.png" alt="star">
								<img class="medium" src="assets/285661_star_icon.png" alt="star">
								<img class="small" src="assets/285661_star_icon.png" alt="star">
							</div>
							<div class="content-quantity">
								<p>${locationObject.acessibilityNote}</p>
								<p>(${locationObject.quantityOfEvaluation})</p>
							</div>
						</div>
						<div class="learn-more-content">
						<p><strong>Endereço: </strong>${address}</p>
						<p><strong>Dias de funcionamento: </strong>${locationObject.typeProduct}</p>
						<p><strong>${p} </strong>${individualNote}</p>
						</div>
				</div>
				<div class="modal-footer">
				</div>
			</div>`
	}
	$('#more').html(template);
	});
});

function sendToast(status, message) {
	var title;
	console.log(status);
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


function resetFields() {
	document.getElementById('nameLocation').value = "";
	document.getElementById('cep-input').value = "";
	document.getElementById('logradouro').value = "";
	document.getElementById('bairro').value = "";
	document.getElementById('numero').value = "";
	document.getElementById('cidade').value = "";
	document.getElementById('estado').value = "";
	document.getElementById('typeOfCuisine').value = "";
	document.getElementById('operatingDays').value = "";
	document.getElementById('startDate').value = "";
	document.getElementById('endDate').value = "";
	document.getElementById('productType').value = "";

}

$('#toAlterPasswordBtn').on('click', function () {
	$('#toAlterPassword').modal('show');
	let firstName = localStorage.getItem('firstName');
	let lastName = localStorage.getItem('lastName');
	let userType = localStorage.getItem('userType');
	let result = userType === '1' ? localStorage.getItem('dateOfBirth') : localStorage.getItem('typeOfDisability');
	let email = localStorage.getItem('email');
	let token = localStorage.getItem('token');
	$('#userFirstName').val(firstName);
	$('#userLast').val(lastName);
	$('#userEmail').val(email);
	$('#userDateOfBirth').val(result);
	$('#userFirstName').prop('disabled', true);
	$('#userLast').prop('disabled', true);
	$('#userEmail').prop('disabled', true);
	$('#userDateOfBirth').prop('disabled', true);
	console.log(firstName, lastName, email);

});

$('#confirmToAlterPassword').on('click', function () {
	let password = $('#userPassword').val();
	let confirmPassword = $('#userConfirmPassword').val();
	let email = $('#userEmail').val();
	let firstName = $('#userFirstName').val();
	let lastName = $('#userLast').val();
	let dateOfBirth = $('#userDateOfBirth').val();
	if (!password || !confirmPassword) {
		sendToast("error", "Confira se todos os campos estão preenchidos.");
		return;
	}

	console.log(password, confirmPassword, email, firstName);

	if (password != confirmPassword) {
		sendToast("error", "As senhas inseridas não correspondem.");
		return;
	}
	let userType = localStorage.getItem('userType');

	let userData = {
		password: password,
		confirmPassword: confirmPassword,
		email: email,
		firstName: firstName,
		lastName: lastName,
		userType: userType,
		dateOfBirth: dateOfBirth
	};

	let apiUrl = './modifyPassword';
	let token = localStorage.getItem('token');
	axios.post(apiUrl, userData, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
			'Authorization': `Bearer ${token}`
		},
	})
		.then((response) => {
			console.log(response.data);
			console.log(data.status);
			var result = response.data;
			if (result.status == 'error') {
				sendToast('error', result.message);
				return;
			}
			sendToast(result.status, result.message);
			customCloseModal('toAlterPassword');

		})
		.catch((error) => {
			console.error(error);
			sendToast("error", "Dados inválidos, tente novamente!");
		});

});

document.getElementById('registerLocation').addEventListener('submit', function (event) {
	event.preventDefault();
	//checar campos vazios, sessão

	var locationName = document.getElementById('nameLocation').value;
	var postalCode = document.getElementById('cep-input').value;
	var street = document.getElementById('logradouro').value;
	var neighborhood = document.getElementById('bairro').value;
	var number = document.getElementById('numero').value;
	var city = document.getElementById('cidade').value;
	var state = document.getElementById('estado').value;

	var address = `${street}, ${number}, ${neighborhood}, ${city}, ${state}`;


	var establishmentType = document.querySelector('input[name="typeOfEstablishment"]:checked') ? document.querySelector('input[name="typeOfEstablishment"]:checked').value : null;

	switch (establishmentType) {
		case 'restaurant':
			var cuisineType = document.getElementById('typeOfCuisine').value;
			var operatingDays = document.getElementById('operatingDays').value;
			console.log(cuisineType, operatingDays);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !cuisineType || !operatingDays) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}
			break;
		case 'event':

			var startDate = document.getElementById('startDate').value;
			var endDate = document.getElementById('endDate').value;
			var eventPrice = document.querySelector('input[name="eventPrice"]:checked') ? document.querySelector('input[name="eventPrice"]:checked').value : null;
			console.log(startDate, endDate, eventPrice);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !startDate || !endDate || !eventPrice) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}

			if (valideDate(startDate)) {
				if (!valideEndDate(startDate, endDate)) {
					sendToast("error", "Data de fim inválida.");
					return;
				}
			}
			else {
				sendToast("error", "Data de início inválida.");
				return;
			}

			break;
		case 'store':
			var productType = document.getElementById('productType').value;
			console.log(productType);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !productType) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}
			break;
	}
	let token = localStorage.getItem('token');
	var apiUrl = './registerLocation';

	var data = {
		locationName: locationName,
		postalCode: postalCode,
		street: street,
		neighborhood: neighborhood,
		number: number,
		city: city,
		state: state,
		establishmentType: establishmentType,
		quantityOfEvaluation: '0'
	};

	switch (establishmentType) {
		case 'restaurant':
			data['cuisineType'] = cuisineType;
			data['operatingDays'] = operatingDays;
			break;
		case 'event':
			data['startDate'] = startDate;
			data['endDate'] = endDate;
			data['eventPrice'] = document.querySelector('input[name="eventPrice"]:checked') ? document.querySelector('input[name="eventPrice"]:checked').value : null;
			break;
		case 'store':
			data['productType'] = productType;
			break;
	}

	var queryString = Object.keys(data).map(key => key + '=' + encodeURIComponent(data[key])).join('&');

	axios.post(apiUrl, queryString, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
			'Authorization': `Bearer ${token}`
		},
	})
		.then((response) => {
			console.log(response.data);
			console.log(data.status);
			var result = response.data;
			if (result.status == 'error') {
				sendToast('error', result.message);
				return;
			}
			resetFields();
			sendToast(result.status, result.message);

			const locationId = result.locationId;
			console.log('ID retornado:', locationId);

			const storedData = JSON.parse(localStorage.getItem('dataLocation'));
			data['id'] = locationId;
			data['placeName'] = locationName;
			data['type'] = establishmentType;

			console.log(data);

			if (establishmentType == 'store') {
				data['typeProduct'] = productType;
			}
			else if (establishmentType == 'restaurant') {
				data['typeOfCuisine'] = cuisineType;
			}
			else {
				price = document.querySelector('input[name="eventPrice"]:checked') ? document.querySelector('input[name="eventPrice"]:checked').value : null;
				if (price == 'free') {
					data['eventPrice'] = 1;
				}
				else {
					data['eventPrice'] = 2;
				}

			}

			if (storedData === null) {

				localStorage.setItem('dataLocation', JSON.stringify(data));
			} else {
				storedData.push(data);
				localStorage.setItem('dataLocation', JSON.stringify(storedData));
			}

			renderLocations(data, address, data.id);

			localStorage.setItem('hasLocation', true);




			$('#registerLocationModal').hide();
			$('#noLocationInfo').hide();
			$('#hasLocation').show();
		})
		.catch((error) => {
			console.error(error);
			sendToast("error", "Dados inválidos, tente novamente!");
		});
});

function valideEndDate(startDate, endDate) {
	var sDate = new Date(startDate);
	var eDate = new Date(endDate);

	if (eDate < sDate) {
		return false;
	}
	else {
		return true;
	}
}

function valideDate(dateString) {
	// Convertendo a string de data para um objeto Date
	var date = new Date(dateString);

	// Obtendo a data atual
	var currentDate = new Date();


	// Verificando se a pessoa tem 18 anos ou mais
	if (date >= currentDate) {
		return true; // A data é válida e a pessoa tem 18 anos ou mais
	} else {
		return false; // A data é válida, mas a pessoa tem menos de 18 anos
	}
}

function formateDate(date) {
	data = new Date(date);
	dataFormatada = data.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
	return dataFormatada;
}

function renderLocations(location, address, mapContainerId, isUpdate) {
	const customId = mapContainerId;
	var template = '';
	if (location.type == 'restaurant') {
		template = `
			
				<div class="header-location-info" id="nameLocal">
					<h2>${location.placeName}</h2>
					<i class="fa-solid fa-utensils customIconUtensils"></i>
				</div>
				<div class="content-location-info" id='infoLocal'>
					<div class="content-stars">
					  <div class="container-star">
						  <div class="my-rating-${mapContainerId}"></div>
					  </div>
					  <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
					</div>
					<p><strong>Endereço:</strong> ${address}</p>
					<p><strong>Dias de Abertura:</strong> ${location.operatingDays}</p>
				</div>
				<!-- Adicione um contêiner para o mapa com um ID exclusivo -->
				<div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
				<div class="btn-section">
					<button class="btn btn-primary" onClick="editLocal(${mapContainerId})">Editar</button><button class="btn btn-danger" onClick="deleteLocal(${mapContainerId})">Deletar</button>
				</div>
			
		`;

	}
	else if (location.type == 'event') {
		startDateFormate = formateDate(location.startDate);
		endDateFormate = formateDate(location.endDate);
		template = `
        	<div class="header-location-info" id="nameLocal"><h2>${location.placeName}</h2>
			<i class="fa-solid fa-champagne-glasses customIconChampagne"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
			<div class="content-stars">
					  <div class="container-star">
						  <div class="my-rating-${mapContainerId}"></div>
					  </div>
					  <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
					</div>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Data de Início:</strong> ${startDateFormate}</p>
            <p><strong>Data de Fim:</strong> ${endDateFormate}</p>
            </div>
             <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
			<div class="btn-section">
			<button class="btn btn-primary" onClick="editLocal(${mapContainerId})">Editar</button><button class="btn btn-danger" onClick="deleteLocal(${mapContainerId})">Deletar</button>
        	</div>
        	`
	}
	else {
		template =
			`
        	
        	<div class="header-location-info" id="nameLocal"><h2>${location.placeName}</h2>
			<i class="fa-solid fa-store customIconStore"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
				<div class="content-stars">
					  <div class="container-star">
						  <div class="my-rating-${mapContainerId}"></div>
					  </div>
					  <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
					</div>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Tipo de Produto:</strong> ${location.typeProduct}</p>
            </div>
            <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
        	<div class="btn-section">
			<button class="btn btn-primary" onClick="editLocal(${mapContainerId})">Editar</button><button class="btn btn-danger" onClick="deleteLocal(${mapContainerId})">Deletar</button>
        	</div>
			
        	 
        `;

	}
	inicializeStars(`.my-rating-${mapContainerId}`, location.acessibilityNote);
	initMap(address, mapContainerId);
	if (isUpdate) {
		$(`#locationInfo-${customId}`).html(template);
		return;
	}
	$('#hasLocation').append(`<div id="locationInfo-${customId}" class="location-info">${template}</div>`);

}

function inicializeStars(id, note) {
	console.log(note);
	setTimeout(() => {
		$(id).starRating({
			initialRating: parseFloat(note),
			readOnly: true,
			strokeColor: '#894A00',
			strokeWidth: 10,
			starSize: 25
		});
	}, 1500)

}

function editLocal(id) {
	const locations = JSON.parse(localStorage.getItem('dataLocation'));
	const location = locations.find((loc) => loc.id == id);
	console.log(location);
	//oculta todos os campos customizados
	$('#rowRestaurant').hide();
	$('#rowEvent').hide();
	$('#rowStore').hide();

	//seta os valores nos campos
	$('#name').val(location.placeName);
	$('#postalCode').val(location.cep);
	$('#publicPlace').val(location.publicPlace);
	$('#number').val(location.number);
	$('#neighborhood').val(location.neighborhood);
	$('#city').val(location.city);
	$('#state').val(location.uf);
	$('#idHidden').val(location.id);
	$('#typeHidden').val(location.type);
	if (location.type == 'restaurant') {
		console.log('é restaurante');
		$('#editTypeOfCuisine').val(location.typeOfCuisine);
		$('#editOperatingDays').val(location.operatingDays);
		$('#rowRestaurant').show();
	}
	else if (location.type == 'event') {
		console.log('é evento');
		var start = formateDate(location.startDate);
		console.log(start);
		$('#editStartDate').val(location.startDate);
		$('#editEndDate').val(location.endDate);
		$('#rowEvent').show();
	}
	else {
		console.log('é loja');
		$('#typeOfProduct').val(location.typeProduct)
		$('#rowStore').show();
	}
	const myModal = document.getElementById('editData');
	const bootstrapModal = new bootstrap.Modal(myModal);
	bootstrapModal.show();

}

function customCloseModal(id) {
	$('body').removeClass('modal-open');
	$('body').attr('style', '');
	$('#' + id).hide();
	$('#' + id).removeClass('show');
	$('.modal-backdrop').removeClass('show');
	$('.modal-backdrop').remove();
}

$('#toAlterButton').on('click', function () {
	var locationName = $('#name').val();
	var postalCode = $('#postalCode').val();
	var street = $('#publicPlace').val();
	var neighborhood = $('#neighborhood').val();
	var number = $('#number').val();
	var city = $('#city').val();
	var state = $('#state').val();
	var id = $('#idHidden').val();
	var type = $('#typeHidden').val();

	var address = `${street}, ${number}, ${neighborhood}, ${city}, ${state}`;
	console.log(address);
	switch (type) {
		case 'restaurant':
			var cuisineType = $('#editTypeOfCuisine').val();
			var operatingDays = $('#editOperatingDays').val();
			console.log(cuisineType, operatingDays);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !cuisineType || !operatingDays) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}
			break;
		case 'event':
			var start = $('#editStartDate').val();
			var end = $('#editEndDate').val();
			console.log(end, start);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !start || !end) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}
			if (valideDate(start)) {
				if (!valideEndDate(start, end)) {
					sendToast("error", "Data de fim inválida.");
					return;
				}
			}


			break;
		case 'store':
			var productType = $('#typeOfProduct').val();
			console.log(productType);
			if (!locationName || !postalCode || !street || !neighborhood || !number || !city || !state || !productType) {
				console.log('Campo vazio');

				sendToast("error", "Verifique se todos os campos estão preenchidos");
				$('.pageload').hide();
				return;
			}
			break;
	}
	var data = {
		locationName: locationName,
		postalCode: postalCode,
		street: street,
		neighborhood: neighborhood,
		number: number,
		city: city,
		state: state,
		locationType: type,
		locationId: id,
	};

	var updateData = {
		"id": id,
		"placeName": locationName,
		"type": type,
	};
	switch (type) {
		case 'restaurant':
			data['cuisineType'] = cuisineType;
			data['operatingDays'] = operatingDays;
			updateData['cuisineType'] = cuisineType;
			updateData['operatingDays'] = operatingDays;
			break;
		case 'event':
			data['startDate'] = start;
			data['endDate'] = end;
			updateData['startDate'] = start;
			updateData['endDate'] = end;
			break;
		case 'store':
			data['productType'] = productType;
			updateData['productType'] = productType;
			break;
	}
	let token = localStorage.getItem('token');
	var url = './alterLocation';
	console.log(data);
	var queryString = Object.keys(data).map(key => key + '=' + encodeURIComponent(data[key])).join('&');

	axios.post(url, queryString, {
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
			'Authorization': `Bearer ${token}`
		},
	})
		.then((response) => {
			console.log(response.data);
			console.log(data.status);
			var result = response.data;
			if (result.status == 'error') {
				sendToast('error', result.message);
				return;
			}
			resetFields();
			sendToast(result.status, result.message);
			console.log("Type: ", updateData.productType);
			renderLocations(updateData, address, id, true)
			customCloseModal('editData');

		})
		.catch((error) => {
			console.error(error);
			sendToast("error", "Dados inválidos, tente novamente!");
		});
});

function deleteLocal(id) {
	const myModal = document.getElementById('deleteData');
	console.log('delete');
	const bootstrapModal = new bootstrap.Modal(myModal);
	bootstrapModal.show();
	$('#deleteButton').attr('onClick', `sendDeleteLocal(${id})`);
}

function updateDelete(array, id) {
	if (array.length === 0) return;
	const newArray = array.filter(objeto => objeto.id !== id);

	// Retorna o array atualizado
	return newArray;
}

function handleSections(array) {
	if (array.length > 0) {
		$('#noLocationInfo').hide();
		$('#hasLocationSection').show();
		return;
	}
	$('#noLocationInfo').show();
	$('#hasLocationSection').hide();
}

function sendDeleteLocal(id) {
	var queryString = 'id=' + encodeURIComponent(id);
	console.log(queryString);
	var apiUrl = './registerLocation';
	var token = localStorage.getItem('token');
	axios.delete(apiUrl, {
		params: {
			id: id
		},
		headers: {
			'Authorization': `Bearer ${token}`
		},
	})
		.then(response => {
			var result = response.data;
			console.log(response.data);
			console.log(result);
			var dataLocation = JSON.parse(localStorage.getItem('dataLocation'));
			let locationsDelete = updateDelete(dataLocation, id);
			localStorage.setItem('dataLocation', JSON.stringify(locationsDelete));
			sendToast(result.status, result.message);
			$('#locationInfo-' + id).hide();
			handleSections(locationsDelete);
			customCloseModal('deleteData');

		})
		.catch(error => {
			console.error('Erro na requisição DELETE:', error);
		});
	console.log('requisição');
}

function checkUserHasLocation(token) {
	console.log('checando locais');
	$('.pageload').show();
	return axios.get('./hasLocation', {
		headers: {
			'Authorization': `Bearer ${token}`
		}
	})
		.then((response) => {
			console.log(response);

			if (response.data.status == "success") {

				localStorage.removeItem('dataLocation');
				localStorage.setItem('dataLocation', JSON.stringify(response.data.locations));
				const locations = response.data.locations;
				$('#noLocationInfo').hide();
				$('#hasLocationSection').show();
				setTimeout(function () {


					locations.map((location, index) => {



						var address = `${location.publicPlace}, ${location.number}, ${location.neighborhood}, ${location.city}, ${location.uf}`;

						renderLocations(location, address, location.id);
						return;
					})
				}, 2000);
				localStorage.setItem('hasLocation', true);

				return;
			}
			localStorage.removeItem('dataLocation');
			localStorage.setItem('hasLocation', false);

		})
		.catch((error) => {
			console.error(error);
			sendToast("error", "Dados inválidos, tente novamente!");
		}).finally(() => {
			$('.pageload').hide();
		});
}

function initMap(address, mapContainerId) {
	const enderecoCadastrado = address;
	const geocoder = new google.maps.Geocoder();

	geocoder.geocode({ address: enderecoCadastrado }, function (results, status) {
		if (status === "OK") {
			const latitude = results[0].geometry.location.lat();
			const longitude = results[0].geometry.location.lng();

			const locationLatLng = new google.maps.LatLng(latitude, longitude);

			const map = new google.maps.Map(document.getElementById(mapContainerId), {
				center: locationLatLng,
				zoom: 15,
				mapTypeControl: false,
				streetViewControl: false,
				fullscreenControl: false,
				zoomControl: false,
				draggable: false,
			});

			// map.addListener('tilesloaded', function() {
			// 	const mapElements = document.querySelectorAll(`#${mapContainerId} .gm-style-cc`);
			// 	mapElements.forEach(function(element) {
			// 		element.style.display = 'none';
			// 	});
			// });

			const marker = new google.maps.Marker({
				position: locationLatLng,
				map: map,
				title: "Local Cadastrado",
				clickable: true,
			});

			marker.addListener("click", function () {
				const url = `https://www.google.com/maps?q=${latitude},${longitude}`;
				window.open(url, "_blank");
			});
		} else {
			console.error("Erro ao geocodificar o endereço:", status);
		}
	});
}

function filterType(type) {
	let data = localStorage.getItem('userType') == '1' ? JSON.parse(localStorage.getItem('dataLocation')) : JSON.parse(localStorage.getItem('locations'));
	var locations = data;
	console.log(locations);
	$('.location-info').hide();
	var filter = locations.filter((location, index) => location.type == type);
	filter.map((location, index) => {
		$('#locationInfo-' + location.id).show();
		return;
	})
		;
};

function generateRandomId() {
	return Math.random().toString(36).substring(2, 15);
}

function loadLocations(isLogged) {
	console.log('loadLocations');
	var apiUrl = './registerLocation';
	axios.get(apiUrl, {
		headers: {
			Authorization: `Bearer ${isLogged}`
		}
	})
		.then(response => {
			var result = response.data;
			console.log(result);
			$('#hasLocationSection').show();
			if (response.data.status == "success") {
				localStorage.setItem('locations', JSON.stringify(response.data.locations));
				const locations = response.data.locations;

				setTimeout(function () {


					locations.map((location, index) => {



						var address = `${location.publicPlace}, ${location.number}, ${location.neighborhood}, ${location.city}, ${location.uf}`;

						renderLocationsEvaluator(location, address, location.id);
						return;
					})
				}, 2000);


				return;
			}


		})
		.catch(error => {
			console.error(error);
		})
		.finally(() => {
			$('.pageload').hide();
		});
}

function createModalContent(locationObject, address) {
	var template =
		`
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title">${locationObject.placeName}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
            <div class="learn-more-stars">
                <img src="assets/285661_star_icon.png" alt="star">
                <p>${locationObject.acessibilityNote}</p>
            </div>
            <div class="learn-more-content">
                <p><strong>Endereço: </strong>${locationObject.address}</p>

                ${locationObject.type == 'restaurant' ? `
                    <p><strong>Dias de funcionamento: </strong>${locationObject.operatingDays}</p>
                    <p><strong>Tipo de Culinária: </strong>${locationObject.typeOfCuisine}</p>
                ` : locationObject.type == 'event' ? `
                    <p><strong>Data de início: </strong>${locationObject.startDate}</p>
                    <p><strong>Data de fim: </strong>${locationObject.endDate}</p>
                    <span class="badge badge-primary">${locationObject.eventPrice == '2' ? 'Pago' : 'Gratuito'}</span>
                ` : `
                    <p><strong>Tipo de Produto: </strong>${locationObject.typeProduct}</p>
                `}
            </div>
        </div>
        <div class="modal-footer">
        </div>
    </div>`;

	$('#learnMore').html(template);
}

function renderLocationsEvaluator(location, address, mapContainerId) {
	console.log('RenderLocationsEvaluator');
	console.log(address);
	const customId = mapContainerId;
	var template = '';
	var buttons = '';

	const disableButton = location.locationEvaluation === 'true' ? 'disabled' : '';
	const buttonAttributes = disableButton ? 'tabindex="0" data-placement="bottom" data-toggle="tooltip" title="Avaliação já realizada"' : '';

	if (location.type == 'restaurant') {
		console.log('é restaurante', location.quantityOfEvaluation);
		console.log('IF');
		template = `
            <div class="header-location-info" id="nameLocal">
                <h2>${location.placeName}</h2>
                <i class="fa-solid fa-utensils customIconUtensils"></i>
            </div>
            <div class="content-location-info" id='infoLocal'>
			
				<div class="content-stars">
				  <div class="container-star">
				  <div class="my-rating-${mapContainerId}"></div>
				  </div>
				  <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
				</div>
                <p><strong>Endereço:</strong> ${address}</p>
                <p><strong>Dias de Abertura:</strong> ${location.operatingDays}</p>
            </div>
            <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
            <div class="btn-section">
                <button id="buttonEvaluate${mapContainerId}" class="btn btn-primary" onClick="evaluateLocation(${mapContainerId})" ${disableButton}>Avaliar</button>
                <button id="learnMoreBtn" class="btn btn-info" data-toggle="modal" data-id="${mapContainerId}">Saiba mais</button>
            </div>
        `;
	}
	else if (location.type == 'event') {
		startDateFormate = formateDate(location.startDate);
		endDateFormate = formateDate(location.endDate);
		template = `
            <div class="header-location-info" id="nameLocal">
                <h2>${location.placeName}</h2>
                <i class="fa-solid fa-champagne-glasses customIconChampagne"></i>
            </div>
            <div class="content-location-info" id='infoLocal'>
				<div class="content-stars">
					<div class="container-star">
				 <div class="my-rating-${mapContainerId}"></div>
					</div>
					 <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
				</div>
                <p><strong>Endereço: </strong> ${address}</p>
				<p><strong>Data de Início:</strong> ${startDateFormate}</p>
	            <p><strong>Data de Fim:</strong> ${endDateFormate}</p>
            </div>
            <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
            <div class="btn-section">
                <button id="buttonEvaluate${mapContainerId}" class="btn btn-primary" onClick="evaluateLocation(${mapContainerId})" ${disableButton}>Avaliar</button>

				<button id="learnMoreBtn" class="btn btn-info" data-toggle="modal" data-id="${mapContainerId}">Saiba mais</button>
            </div>
        `;
	}
	else {
		template =
			`
        	<div class="header-location-info" id="nameLocal"><h2>${location.placeName}</h2>
			<i class="fa-solid fa-store customIconStore"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
			<div class="content-stars">
			<div class="container-star">
				 <div class="my-rating-${mapContainerId}"></div>
			</div>
			 <p id="pMyRating-${mapContainerId}">(${location.quantityOfEvaluation})</p>
			</div>
			<p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Tipo de Produto:</strong> ${location.typeProduct}</p>
            </div>
			<!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
            <div class="btn-section">
                <button id="buttonEvaluate${mapContainerId}" class="btn btn-primary" onClick="evaluateLocation(${mapContainerId})" ${disableButton}>Avaliar</button>
				<button id="learnMoreBtn" class="btn btn-info" data-toggle="modal" data-id="${mapContainerId}">Saiba mais</button>
            </div>
        `;
	}

	console.log(location, template);
	inicializeStars(`.my-rating-${mapContainerId}`, location.acessibilityNote);
	console.log(location.acessibilityNote);
	initMap(address, mapContainerId);

	$('#cardLocation').append(`<div id="locationInfo-${customId}" class="location-info">${template}</div>`);
}

function evaluateLocation(id) {

	console.log(id);

	$('#idLocationHidden').val(id);

	let apiUrl = './standard';

	axios.get(apiUrl)
		.then(response => {
			$('#questions').html('');

			const myModal = document.getElementById('evaluateData');

			const bootstrapModal = new bootstrap.Modal(myModal);
			bootstrapModal.show();
			const modalTitle = document.querySelector('.modal-title');
			const standardName = response.data[0].standard_name;

			modalTitle.textContent = standardName + ' - Avalie este local';
			const questions = response.data[0].questions;

			const modalBody = document.querySelector('.modal-body');
			var template = '';
			var values = [];
			questions.map((question, index) => {
				values.push(question.question_id);
				template += `<div>
			<p>${index + 1}. ${question.question_text}<\p>
			<div class="input-group mb-3">
				<div class="input-group-text">
					<input type='radio' id="answer-${question.question_id}-1" name="answer-${question.question_id}" value="2"><label for="answer-${question.question_id}-1">Suficiente</label>
				</div>
				<div class="input-group-text">
					<input type='radio' id="answer-${question.question_id}-2" name="answer-${question.question_id}" value="1"><label for="answer-${question.question_id}-2">Parcial</label>
				</div>
				<div class="input-group-text">
					<input type='radio' id="answer-${question.question_id}-3" name="answer-${question.question_id}" value="0"><label for="answer-${question.question_id}-3">Insuficiente</label>
				</div>
			</div>
			<\div>`;
			})
			localStorage.setItem('questionList', values);
			console.log(template);
			$('#questions').append(template);

		})
		.catch(error => {
			console.error(error);
		})
		.finally(() => {
			$('.pageload').hide();
		});
	console.log("Entranndo na avaliação.");
}










