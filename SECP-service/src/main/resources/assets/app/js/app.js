// Declare app level module which depends on filters, and services
angular.module('SECP', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.date', 'routeStyles'])
  .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/home/home.html',
        controller: 'HomeController',
        css: 'css/home.css',
      })
      .when('/login', {
        templateUrl: 'views/login/login.html',
        controller: 'LoginController',
        css: 'css/login.css',
      })
      .when('/register', {
        templateUrl: 'views/register/register.html',
        controller: 'RegisterController',
        css: 'css/register.css',
      })
      .when('/chats', {
        templateUrl: 'views/chat/chats.html',
        controller: 'ChatController',
        css: 'css/chat.css',
      })
      .otherwise({ redirectTo: '/' });

      // use the HTML5 History API
      $locationProvider.html5Mode({
             enabled: true,
             requireBase: false
      });
  }]);

