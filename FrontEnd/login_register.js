function displayLoginForm() {
    document.getElementById('register').className = 'hiddenForm';
    document.getElementById('login').classList.remove('hiddenForm');
}

function displayRegisterForm() {
    document.getElementById('login').className = 'hiddenForm';
    document.getElementById('register').classList.remove('hiddenForm');
}