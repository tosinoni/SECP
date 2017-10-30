// Declare app level module which depends on filters, and services
angular.module('SECP', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.date', 'angularCSS'])
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/home/home.html',
        controller: 'HomeController',
        css: "css/home.css"
      })
      .when('/login-register', {
        templateUrl: 'views/login-register/login-register.html',
        controller: 'LoginRegisterController',
        css: 'css/login_register.css'
      })
      .otherwise({ redirectTo: '/' });
  }]);

