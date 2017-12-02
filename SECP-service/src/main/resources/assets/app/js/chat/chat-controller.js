'use strict';

angular.module('SECP')
  .controller('ChatController', ['$scope', 'Chat', 'atmosphereService',
    function ($scope, Chat, atmosphereService) {
       console.log("here")
      //declaring variables
      $scope.contacts = [];
      $scope.currentUser = Chat.getCurrentUser();


      Chat.getChatList().then(function(data) {
        $scope.contacts = data;
      });

        $scope.sendMessageUsingEnter = function() {
            var code = event.keyCode || event.which;
            if (code === 13) {
                if (!event.shiftKey) {
                    event.preventDefault();
                    $scope.sendMessage();
                }
            }
        };

      $scope.contactSelected = function(contact) {
         $scope.selectedChat = contact;
         Chat.getMessages(contact).then(function(data) {
            $scope.messages = data;
         });
      };

      $scope.model = {
                  transport: 'websocket',
                  messages: []
       };

              var socket;
              //user=angular.fromJson($cookies.get('user_details_object'))['user_name'];
              var groupId = 2;
              var pingUrl='chat/' + groupId
              var request = {
                  url: "http://localhost:8080/" + pingUrl ,
                  contentType: 'application/json',
                  logLevel: 'debug',
                  transport: 'websocket',
                  trackMessageLength: true,
                  reconnectInterval: 5000,
                  fallbackTransport: "long-polling"
              };

              request.onOpen = function(response){
                  $scope.model.transport = response.transport;
                  $scope.model.connected = true;
                  $scope.model.content = 'Atmosphere connected using ' + response.transport;
              };

              request.onClientTimeout = function(response){
                  $scope.model.content = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval;
                  $scope.model.connected = false;
                  socket.push(atmosphere.util.stringifyJSON({ author: "author", message: 'is inactive and closed the connection. Will reconnect in ' + request.reconnectInterval }));
                  setTimeout(function(){
                      socket = atmosphereService.subscribe(request);
                  }, request.reconnectInterval);
              };

              request.onReopen = function(response){
                  $scope.model.connected = true;
                  $scope.model.content = 'Atmosphere re-connected using ' + response.transport;
              };

              //For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
              request.onTransportFailure = function(errorMsg, request){
                  atmosphere.util.info(errorMsg);
                  console.log(request);
                  request.fallbackTransport = 'long-polling';
                  $scope.model.header = 'Atmosphere Chat. Default transport is WebSocket, fallback is ' + request.fallbackTransport;
              };

              request.onMessage = function(response){
                  var responseText = response.responseBody;
                  try{
                      var message = atmosphere.util.parseJSON(responseText);

                      var date = typeof(message.time) === 'string' ? parseInt(message.time) : message.time;
                      $scope.$evalAsync(function () {
                         $scope.model.messages.push({author: message.author, date: new Date(date), text: message.message});
                       });

                  }catch(e){
                      console.error("Error parsing JSON: ", responseText);
                      throw e;
                  }
              };

              request.onClose = function(response){
                  $scope.model.connected = false;
                  $scope.model.content = 'Server closed the connection after a timeout';
                  socket.push(atmosphere.util.stringifyJSON({ author: $scope.model.name, message: 'disconnecting' }));
              };

              request.onError = function(response){
                  $scope.model.content = "Sorry, but there's some problem with your socket or the server is down";
                  $scope.model.logged = false;
              };

              request.onReconnect = function(request, response){
                  $scope.model.content = 'Connection lost. Trying to reconnect ' + request.reconnectInterval;
                  $scope.model.connected = false;
              };

              socket = atmosphereService.subscribe(request);

              $scope.notifyClient=function(name){
                      console.log("=="+name);

                      //send private message here
                      socket.push(atmosphere.util.stringifyJSON({author:name, message: "hello"}));
              }

                    $scope.sendMessage = function() {
              //         var message = {
              //            'text': $scope.messageInput
              //         };

                        console.log("=="+$scope.currentUser);


                                             //send private message here
                                             socket.push(atmosphere.util.stringifyJSON({author:$scope.currentUser, message: "hello"}));
                       //$scope.messages.slice(-1)[0].contents.push(message)
                       //clearing the message input in the textarea
                       $scope.messageInput = null;
                    };


    }])
