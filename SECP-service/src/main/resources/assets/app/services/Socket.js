'use strict';

angular.module('SECP')
  .factory('Socket', function($rootScope, $location) {
    let host = location.host;
    let protocol = "wss";

    if($location.protocol() === "http") {
        protocol = "ws";
    }

      var service = {};
      service.callbacks = [];

      function socketCloseListener() {
          if($rootScope.isAuthenticated) {
              let userID = localStorage.getItem('userID');
              //var url = protocol + "://" + host + "/chat/" + userID;
              var url = "ws://localhost:8080/chat/" + userID;

              let newWs = new WebSocket(url);
              newWs.onmessage = function (event) {
                  angular.forEach(service.callbacks, function (callback) {
                      callback(event.data);
                  });
              };
              newWs.onclose = socketCloseListener;

              service.ws = newWs;
          }
      }
      function connect() {
          let userID = localStorage.getItem('userID');
          //var url = protocol + "://" + host + "/chat/" + userID;
          var url = "ws://localhost:8080/chat/" + userID;

          if (service.ws)
              return;

          var ws = new WebSocket(url);
          ws.onmessage = function (event) {
              angular.forEach(service.callbacks, function (callback) {
                  callback(event.data);
              });
          };
          ws.onclose = socketCloseListener;

          service.ws = ws;
      }
      return {
          connect : connect,

          send : function (message) {
              message.senderDeviceName = new Fingerprint().get();
              service.ws.send(JSON.stringify(message));
              },
          subscribe : function (callback) {
              service.callbacks.push(callback);
          }

    }
});
