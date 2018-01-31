'use strict';

angular.module('SECP')
    .directive('profile', function (Group) {
    return {
        restrict: 'E',
        scope: {
            id: '=',
            modalname: '@'
         },
        templateUrl: 'directives/profiles/profile.html',
        link: function ($scope, element, attrs) {
            $scope.isPrivateChat = false;
            $scope.$watch('id', function(id) {
                Group.getProfile(id).then(function(group) {
                    if(group) {
                        //If there are only two users in the group, that means that there
                        //should not be an edit or member section.
                        if(group.groupType === "PRIVATE"){
                            $scope.isPrivateChat = true;
                            const localUserID = localStorage.getItem('userID');
                            for(let user of group.users){
                                if(user.userID != localUserID){
                                    //on load, if the selected group is a private group,
                                    //error msg reads that scope.group is undefined. To counteract this,
                                    //need to set it to an object first.
                                    $scope.profile = {};
                                    $scope.profile.avatarUrl = user.avatarUrl;
                                    $scope.profile.displayName = user.displayName;
                                    $scope.profile.permissions = {'0': user.permission};
                                    $scope.profile.roles = user.roles;
                                }
                            }
                        }
                        else{
                            $scope.profile = group;
                            $scope.isPrivateChat = false;
                        }  
                    }
                });

            });

            $("#profileBlock").niceScroll({autohidemode:'leave'});
            $scope.editProfile = function(){
                console.log("we are editting the profile now");

                // CHECK TO SEE IF USER OR GROUP. IF USER, CHECK TO SEE IF THE USER PROFILE BELONGS TO THE PERSON LOOKING AT IT.
                // THEN OPEN EDITABLE. IF GROUP, OPEN EDITABLE.
                // EDITABLE WOULD BE TO EDIT THE IMAGE AND THE NAME
            }
            $scope.closeProfile = function(){
                document.getElementById("chatBlock").setAttribute("class", "col-md-8");
                $("#profileBlock").hide();
            }
        } //DOM manipulation
    };
});
