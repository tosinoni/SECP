'use strict';

angular.module('SECP')
    .directive('chatMessage', function (EncryptionService) {
    return {
        restrict: 'E', //E = element, A = attribute, C = class, M = comment
        scope: {
            //@ reads the attribute value, = provides two-way binding, & works with functions
            messages: '=',
            currentUser: '=',
            selectedChat: '=',
            secretKeys: '=',
            clicked: '='
         },
        templateUrl: 'directives/chat-message/chat-message.html',
        link: function ($scope, element, attrs) {

            var getTime = function(time) {
                if(time) {
                var date = new Date(time);
                    return moment(date).startOf('hour').fromNow();
                }
            }

             var getMessageObject = function(message) {
                var obj = {};
                obj.id = message.messageId;
                obj.senderId = message.senderId;
                obj.timestamp = getTime(message.timestamp);
                obj.contents = [message];

                if(message.senderId && $scope.users) {
                    var sender = $scope.users[message.senderId];
                    obj.displayName = sender.displayName;
                    obj.userImg = sender.avatarUrl;
                }
                return obj;
             }

             var formatMessages = function(messages) {
                var formatedMessages = [];
                if (messages) {
                    var currentMessageObj;
                    for (var message of messages) {
                        if(!currentMessageObj) {
                            currentMessageObj = getMessageObject(message);
                        } else if(currentMessageObj.senderId !== message.senderId) {
                            formatedMessages.push(currentMessageObj);
                            currentMessageObj = getMessageObject(message);
                        } else {
                            currentMessageObj.contents.push(message);
                        }
                    }
                }

                if (currentMessageObj) {
                    formatedMessages.push(currentMessageObj)
                }

                return formatedMessages;
             }

            $scope.$watch('messages', function(newMessages) {
                if(!_.isEmpty($scope.secretKeys)) {
                    $scope.formatedMessages = formatMessages(newMessages);
                    //move the scroll button down to see the latest message
                    $('#chat-scroll').animate({
                        scrollTop: $('#chat-scroll').get(0).scrollHeight
                    });
                }
             }, true);


             $scope.$watch('selectedChat', function(selectedChat) {
                  if(selectedChat && selectedChat.users) {

                     if(selectedChat.groupType == "PRIVATE") {
                        for (var user of selectedChat.users) {
                            if($scope.currentUser.userID !== user.userID) {
                                $scope.selectedChat.displayName = user.displayName;
                                $scope.selectedChat.avatarUrl = user.avatarUrl;
                            }
                        }
                     }
                     var obj = {};
                     for(var user of selectedChat.users) {
                         obj[user.userID] = user;
                     }

                     $scope.users = obj;
                  }
             });

            $scope.getDecryptedMessage = function(message) {
                var groupId = $scope.selectedChat.groupID;
                if (message && !_.isEmpty($scope.secretKeys)) {
                    return EncryptionService.decryptMessage(message, $scope.secretKeys[groupId]);
                }

            }
             $scope.clickProfile = function(){

                // NEED TO ADD CHECK TO SEE IF USER OR PROFILE, THEN GENERATE USER OR PROFILE DIV.
                $scope.clicked = !$scope.clicked;
                var chatBlock = document.getElementById("chatBlock");
                var profileBlock = $("#profileBlock");
                if($scope.clicked === false){
                    chatBlock.setAttribute("class", "col-md-8");
                    profileBlock.hide();
                }
                else{
                    chatBlock.setAttribute("class", "col-md-4");
                    profileBlock.fadeIn("slow");
                    profileBlock.show();
                }
             };
        } //DOM manipulation
    }
});
