'use strict';

angular.module('SECP')
    .directive('uniqueFieldValidation', function ($http, $q, $timeout) {
    return {
        restrict: 'A', //E = element, A = attribute, C = class, M = comment
        require: 'ngModel',

        link: function ($scope, element, attr, ngModel) {
            // fetch the url from directives 'uniqueFieldValidation' attribute
            var url = attr.uniqueFieldValidation;

            ngModel.$asyncValidators.invalidInputValue = function(modelValue, viewValue) {
                var inputParamValue = viewValue;
                var deferred = $q.defer();

                // ask the server if this username exists
                $http.get(attr.uniqueFieldValidation + inputParamValue)
                .then(function(response) {
                     // simulate a server response delay of half a second
                        $timeout(function() {
                          if (response.status === 200) {
                            deferred.reject();
                          } else {
                            deferred.resolve();
                          }
                        }, 500);
                 });

                // return the promise of the asynchronous validator
                return deferred.promise;
            }
        } //DOM manipulation
    }
});
