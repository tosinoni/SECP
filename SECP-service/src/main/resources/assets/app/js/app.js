// Declare app level module which depends on filters, and services
angular.module('SECP', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.date', 'angularCSS'])
  .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/home/home.html',
        controller: 'HomeController'
      })
      .when('/login', {
        templateUrl: 'views/login/login.html',
        controller: 'LoginController'
      })
      .when('/register', {
        templateUrl: 'views/register/register.html',
        controller: 'RegisterController'
      })
      .otherwise({ redirectTo: '/' });

      // use the HTML5 History API
      $locationProvider.html5Mode({
             enabled: true,
             requireBase: false
      });
  }]);

