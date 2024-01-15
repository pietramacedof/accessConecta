$(document).ready(function () {

    $('a[href^="#"]').on('click', function (e) {
        e.preventDefault();
        var target = this.hash;
        var $target = $(target);

        $('html, body').stop().delay(800).animate({
            'scrollTop': $target.offset().top
        }, 400, 'swing', function () {
            window.location.hash = target;
        });
    });
    $('#liveToastBtn').on('click', function(e){
        e.preventDefault();
        console.log("click")
        $('#liveToast').toast('show');
    })
});

document.addEventListener('DOMContentLoaded', function () {
    const lightModeIcon = document.getElementById('lightModeIcon');
    const darkModeIcon = document.getElementById('darkModeIcon');
    const sections = document.querySelectorAll('section');
    const logoDark = document.getElementById('logo-dark');
    const logoLight = document.getElementById('logo-light');
    const lightLogo = document.getElementById('light-logo');
    const darkLogo = document.getElementById('dark-logo');
    const registerModal = document.getElementById('registerModal');
    const loginModal = document.getElementById('loginModal');
    const logoLogin = document.getElementById('logo-login');
    const logoDarkLogin = document.getElementById('logo-login-dark');

    lightModeIcon.addEventListener('click', function () {
        console.log('Light Mode Clicked');
        lightModeIcon.style.display = 'none';
        darkModeIcon.style.display = 'block';
        logoDark.style.display = 'block';
        logoLogin.style.display = 'none';
        logoDarkLogin.style.display = 'block';
        logoLight.style.display = 'none';
        darkLogo.style.display = 'block';
        lightLogo.style.display = 'none';
        registerModal.classList.remove('dark-mode');
        sections.forEach(function (section) {
        section.classList.remove('dark-mode');
        });
        loginModal.classList.remove('dark-mode');
    });


    darkModeIcon.addEventListener('click', function () {
        console.log('Dark Mode Clicked');
        lightModeIcon.style.display = 'block';
        darkModeIcon.style.display = 'none';
        logoLogin.style.display = 'block';
        logoDarkLogin.style.display = 'none';
        logoDark.style.display = 'none';
        logoLight.style.display = 'block';
        darkLogo.style.display = 'none';
        lightLogo.style.display = 'block';
        registerModal.classList.add('dark-mode');
        sections.forEach(function (section) {
            section.classList.add('dark-mode');
        });
        loginModal.classList.add('dark-mode');
    });
});

document.addEventListener('DOMContentLoaded', function () {
    // Botões e modal
    const openModalBtn = document.getElementById('modalLogin');
    const loginModal = document.getElementById('loginModal');
    const close = document.getElementById('closeLogin');

 
    openModalBtn.addEventListener('click', function () {
        loginModal.style.display = 'block';
    });

    close.addEventListener('click', function () {
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
 
 document.getElementById('signUp').addEventListener('click', function() {
    console.log('oi');
  
    // Close the login modal:
    const loginModal = document.getElementById('loginModal');
    loginModal.style.display = 'none';
  
    // Open the register modal (if applicable, using vanilla JavaScript):
    const registerModal = document.getElementById('registerModal');
    registerModal.style.display = 'block';  // Assuming you're using 'display: none' to hide it initially
 
  });

  document.getElementById('redirectLogin').addEventListener('click', function() {
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
    openModalBtn.addEventListener('click', function () {
        registerModal.style.display = 'block';
    });

    // Fechar o modal ao clicar no botão de fechar
    closeModalBtn.addEventListener('click', function () {
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
    cadastroForm.addEventListener('submit', function (event) {
        event.preventDefault();
        // Adicione a lógica de envio do formulário aqui
        console.log('Formulário enviado!');
        // Feche o modal após o envio
        registerModal.style.display = 'none';
    });
});

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('togglePassword').addEventListener('click', function (e) {
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
    document.getElementById('loginPassword').addEventListener('click', function (e) {
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
    cadastrarLocalModal.addEventListener('click', function () {
        cadastrarLocalModal.style.display = 'block';
    });
});
	

