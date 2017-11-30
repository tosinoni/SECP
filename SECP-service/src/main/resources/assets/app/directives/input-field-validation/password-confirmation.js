'use strict';

angular.module('SECP')
    .directive('passwordConfirmation', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue, $scope) {
                var noMatch = viewValue != scope.frm.password.$viewValue;
                ctrl.$setValidity('noMatch', !noMatch);
                return viewValue;
            })
        } //DOM manipulation
    }
});
