'use strict';

angular.module('SECP')
    .directive('groupProfile', function (Group) {
    return {
        restrict: 'E',
        scope: {
            id: '=',
            modalname: '@'
         },
        templateUrl: 'directives/profiles/group-profile.html',
        link: function ($scope, element, attrs) {
            $scope.isPrivateChat = false;
            $scope.$watch('id', function(id) {
                Group.getProfile(id).then(function(group) {
                    if(group) {
                        //If there are only two users in the group, that means that there
                        //should not be an edit or member section.
                        if(group.users.length === 2){
                            $scope.isPrivateChat = true;
                            const localUserID = localStorage.getItem('userID');
                            for(let user in group.users){
                                console.log("HI USER " + user.displayName);
                                if(user.userID != localUserID){
                                    console.log("HI USER WITH ID" + user.displayName);
                                }
                            }
                        }
                        else{
                            $scope.group = group;
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
