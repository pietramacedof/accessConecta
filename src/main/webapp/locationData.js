

$(document).ready(function() {

	const isLogged = localStorage.getItem('token');

	if (!isLogged) {
		localStorage.clear();
		window.location.href = './';
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
			checkUserHasLocation(isLogged);
		}
	}).catch((error) => {
		console.error(error);
	}).finally(() => {
		$('.pageload').hide();
	});

	$('a[href^="#"]').on('click', function(e) {
		e.preventDefault();
		var target = this.hash;
		var $target = $(target);

		$('html, body').stop().delay(800).animate({
			'scrollTop': $target.offset().top
		}, 400, 'swing', function() {
			window.location.hash = target;
		});
	});
	$('#liveToastBtn').on('click', function(e) {
		e.preventDefault();
		console.log("click")
		$('#liveToast').toast('show');
	})
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

document.getElementById('registerLocation').addEventListener('submit', function(event) {
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
			var data = response.data;
			console.log(data.status);
			if (data.status === 'error') {
				sendToast('error', data.message);
				return;
			}
			resetFields();
			sendToast(data.status, data.message);
			localStorage.setItem('localData', JSON.stringify(data));
			localStorage.setItem('hasLocation', true);


			// Remova as classes existentes no ícone

			if (establishmentType === 'restaurant') {

				$('#hasLocation').append(`<div class="info-geral">
        	<div id="locationInfo" class="location-info">
        	<div class="header-location-info" id="nameLocal"><h2>${locationName}</h2><i class="fa-solid fa-utensils customIconUtensils"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Dias de Abertura:</strong> ${operatingDays}</p>
            </div>
        	</div>
        </div>`);


			}
			else if (establishmentType === 'event') {
				startDateFormate = formateDate(startDate);
				endDateFormate = formateDate(endDate);

				$('#hasLocation').append(`<div class="info-geral">
        	<div id="locationInfo" class="location-info">
        	<div class="header-location-info" id="nameLocal"><h2>${locationName}</h2><i class="fa-solid fa-champagne-glasses customIconChampagne"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Data de Início:</strong> ${startDateFormate}</p>
            <p><strong>Data de Fim:</strong> ${endDateFormate}</p>
            </div>
        	</div>
        </div>`);

			}
			else {
				$('#hasLocation').append(`<div class="info-geral">
        	<div id="locationInfo" class="location-info">
        	<div class="header-location-info" id="nameLocal"><h2>${locationName}</h2><i class="fa-solid fa-store customIconStore"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Tipo de Produto:</strong> ${productType}</p>
            </div>
        	</div>
        </div>`);

			}

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

function displayLocationInformation() {

	if (localStorage.getItem('hasLocation') === 'true') {

		const localData = JSON.parse(localStorage.getItem('localData'));



	} else {

	}
}

// Call the function whenever you need to check and display location information
displayLocationInformation();

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


				localStorage.setItem('dataLocation', JSON.stringify(response.data.locations));
				const locations = response.data.locations;
				$('#noLocationInfo').hide();
				$('#hasLocationSection').show();
				setTimeout(function() {


					locations.map((location, index) => {


						const mapContainerId = `mapContainer${index}`;
						var address = `${location.publicPlace}, ${location.number}, ${location.neighborhood}, ${location.city}, ${location.uf}`;
						if (location.type == 'restaurant') {
							$('#hasLocation').append(`
					        	<div id="locationInfo" class="location-info">
					        		<div class="header-location-info" id="nameLocal">
					        			<h2>${location.placeName}</h2><i class="fa-solid fa-utensils customIconUtensils"></i>
					        		</div>
				            		<div class="content-location-info" id='infoLocal'>
				            			<p><strong>Endereço:</strong> ${address}</p>
				            			<p><strong>Dias de Abertura:</strong> ${location.operatingDays}</p>
				            		</div>
				            		<!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            						<div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
					        	</div>
        					`);

							initMap(address, mapContainerId);
						} else if (location.type == 'event') {
							startDateFormate = formateDate(location.startDate);
							endDateFormate = formateDate(location.endDate);
							$('#hasLocation').append(`
        	<div id="locationInfo" class="location-info">
        	<div class="header-location-info" id="nameLocal"><h2>${location.placeName}</h2><i class="fa-solid fa-champagne-glasses customIconChampagne"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Data de Início:</strong> ${startDateFormate}</p>
            <p><strong>Data de Fim:</strong> ${endDateFormate}</p>
            </div>
             <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
        	</div>
        	
        `);
							initMap(address, mapContainerId);
						} else {
							$('#hasLocation').append(`
        	<div id="locationInfo" class="location-info">
        	<div class="header-location-info" id="nameLocal"><h2>${location.placeName}</h2><i class="fa-solid fa-store customIconStore"></i>
        	</div>
            <div class="content-location-info" id='infoLocal'>
            <p><strong>Endereço:</strong> ${address}</p>
            <p><strong>Tipo de Produto:</strong> ${location.typeProduct}</p>
            </div>
            <!-- Adicione um contêiner para o mapa com um ID exclusivo -->
            <div id="${mapContainerId}" class="map-container" style="height: 200px;"></div>
        	</div>
        	 
        `);
							initMap(address, mapContainerId);
						}

						return;
					})
				}, 2000);
				localStorage.setItem('hasLocation', true);

				return;
			}
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

	geocoder.geocode({ address: enderecoCadastrado }, function(results, status) {
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

			map.addListener('tilesloaded', function() {
				const mapElements = document.querySelectorAll(`#${mapContainerId} .gm-style-cc`);
				mapElements.forEach(function(element) {
					element.style.display = 'none';
				});
			});

			const marker = new google.maps.Marker({
				position: locationLatLng,
				map: map,
				title: "Local Cadastrado",
				clickable: true,
			});

			marker.addListener("click", function() {
				const url = `https://www.google.com/maps?q=${latitude},${longitude}`;
				window.open(url, "_blank");
			});
		} else {
			console.error("Erro ao geocodificar o endereço:", status);
		}
	});
}











