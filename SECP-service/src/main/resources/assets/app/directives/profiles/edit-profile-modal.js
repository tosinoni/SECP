'use strict';

angular.module('SECP')
    .directive('editProfileModal', function () {
    return {
        restrict: 'E',
        scope: {
            displayname: "@",
            imgurl: "@"
         },
        templateUrl: 'directives/profiles/edit-profile-modal.html',
        link: function ($scope, element, attrs) {
            $scope.openUploadFileDialog = function(){
                var frm = document.form1.upload;
                if(document.all && document.getElementById){
                    document.form1.upload.click();
                }
            }
        }
    };
});
