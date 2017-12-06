// Declare app level module which depends on filters, and services
angular.module('SECP', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.date', 'routeStyles', 'angular-jwt'])
  .config(function ($routeProvider, $locationProvider, jwtOptionsProvider, $httpProvider) {

    //configuring authentication
    jwtOptionsProvider.config({
        unauthenticatedRedirectPath: '/login',
        tokenGetter: ['Auth', function(Auth) {
            if (Auth.isTokenExpired()) {
                return null;
            }

            return localStorage.getItem('token');
        }]
    });
    $httpProvider.interceptors.push('jwtInterceptor');


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
        requiresLogin: true
      })
      .when('/portal', {
        templateUrl: 'views/portal/portal.html',
        controller: 'PortalController',
        css: 'css/portal.css'
      })
      .when('/portal/audit', {
        templateUrl: 'views/portal/audit.html',
        controller: 'PortalController',
        css: 'css/portal.css'
      })
      .when('/portal/audit/user', {
          templateUrl: 'views/portal/audit-user.html',
          controller:'PortalController',
          css: 'css/portal.css',
          js: 'portal/portal-controller.js'
      })
      .when('/portal/audit/group', {
          templateUrl: 'views/portal/audit-group.html',
          controller:'PortalController',
          css: 'css/portal.css',
          js: 'portal/portal-controller.js'
      })
      .when('/portal/manage', {
          templateUrl: 'views/portal/manage.html',
          controller:'PortalController',
          css: 'css/portal.css',
          js: 'portal/portal-controller.js'
      })
      .when('/portal/manage/user', {
          templateUrl: 'views/portal/manage-user.html',
          controller:'PortalController',
          css: 'css/portal.css',
          js: 'portal/portal-controller.js'
      })
      .when('/error/404', {
        templateUrl: 'views/error/404.html'
      })
      .otherwise({
        templateUrl: 'views/error/404.html'
      });
  
      // use the HTML5 History API
      $locationProvider.html5Mode({
             enabled: true,
             requireBase: false
      });
  })
  .run(function($rootScope,Auth,authManager) {
      $rootScope.logout = function() {
        Auth.logout();
        location.reload();
      }

      authManager.checkAuthOnRefresh();
      authManager.redirectWhenUnauthenticated();
  });

