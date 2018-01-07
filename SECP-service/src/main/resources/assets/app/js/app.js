// Declare app level module which depends on filters, and services
angular.module('SECP', ['ngResource', 'ngRoute', 'ui.bootstrap', 'ui.date',
    'routeStyles', 'angular-jwt', 'ngWebCrypto', 'angular-uuid', 'datatables'])
  .config(function ($routeProvider, $locationProvider, jwtOptionsProvider, $httpProvider) {

    //add isAdmin function
    var isAdmin = function(Auth, $location) {
        return Auth.isUserAnAdmin().then(function(res){
            if (!res) {
                $location.path('/error/404');
            }
        });
    };

    //configuring authentication
    jwtOptionsProvider.config({
        unauthenticatedRedirectPath: '/login',
        authPrefix: '',
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
      .when('/authenticate', {
        templateUrl: 'views/login/authenticate.html',
        controller: 'AuthenticateController',
        css: 'css/login.css'
      })
      .when('/register', {
        templateUrl: 'views/register/register.html',
        controller: 'RegisterController',
        css: 'css/register.css',
        requiresLogin: true,
        resolve: {
            isAdmin: isAdmin
        }
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
        css: 'css/portal.css',
        requiresLogin: true,
        resolve: {
            isAdmin: isAdmin
        }
      })
      .when('/portal/audit', {
        templateUrl: 'views/portal/audit.html',
        controller: 'PortalController',
        css: 'css/portal.css',
        requiresLogin: true,
        resolve: {
            isAdmin: isAdmin
        }
      })
      .when('/portal/audit/user', {
          templateUrl: 'views/portal/audit-user.html',
          controller:'PortalController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
            isAdmin: isAdmin
          }
      })
      .when('/portal/audit/group', {
          templateUrl: 'views/portal/audit-group.html',
          controller:'PortalController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
            isAdmin: isAdmin
          }
      })
      .when('/portal/manage', {
          templateUrl: 'views/portal/manage.html',
          controller:'PortalController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
            isAdmin: isAdmin
          }
      })
      .when('/portal/manage/user', {
          templateUrl: 'views/portal/manage-user.html',
          controller:'PortalController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
            isAdmin: isAdmin
          }
      })
      .when('/portal/manage/group', {
          templateUrl: 'views/portal/manage-group.html',
          controller:'PortalController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
            isAdmin: isAdmin
          }
      })
      .when('/portal/configure/filter', {
          templateUrl: 'views/portal/configure-filter.html',
          controller:'FilterController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
              isAdmin: isAdmin
          }
      })
      .when('/portal/configure', {
          templateUrl: 'views/portal/configure.html',
          controller:'ConfigureController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
              isAdmin: isAdmin
          }
      })
      .when('/portal/configure/tags', {
          templateUrl: 'views/portal/configure-tags.html',
          controller:'ConfigureController',
          css: 'css/portal.css',
          requiresLogin: true,
          resolve: {
              isAdmin: isAdmin
          }
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
      $(document).click(function(event) {
          $(".navbar-collapse").collapse('hide');
      });
      $rootScope.logout = function() {
        Auth.logout();
        location.reload();
      }

      authManager.checkAuthOnRefresh();
      authManager.redirectWhenUnauthenticated();

      $rootScope.getHomeUrl = function() {
        if(Auth.isTokenExpired()) {
            return '/';
        } else if(!$rootScope.isAdmin) {
            return '/chats'
        }
        return '/portal';
      }

      $rootScope.$on("$locationChangeStart", function(event) {
        // handle route changes
        if(!Auth.isTokenExpired()) {
            Auth.isUserAnAdmin().then(function(res){
                $rootScope.isAdmin = res;
            });
        }
      });
  });

