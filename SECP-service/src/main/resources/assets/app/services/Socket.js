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

      return {
          connect : function () {
              let userID = localStorage.getItem('userID');
              //var url = protocol + "://" + host + "/chat/" + userID;
              var url = "ws://localhost:8080/chat/" + userID;

              if (service.ws)
                  return;

              console.log(url)

              var ws = new WebSocket(url);
              ws.onmessage = function (event) {
                  angular.forEach(service.callbacks, function (callback) {
                      callback(event.data);
                  });
              };
              service.ws = ws;
          },
          send : function (message) {
              message.senderDeviceName = new Fingerprint().get();
              service.ws.send(JSON.stringify(message));
              },
          subscribe : function (callback) {
              service.callbacks.push(callback);
          }

    }
    //   return {
    //     onopen: function (callback) {
    //       socket.onopen = function () {
    //         $rootScope.$apply(function () {
    //           callback.apply(socket);
    //         });
    //       };
    //     },
    //
    //     onmessage: function (callback) {
    //       socket.onmessage = function (e) {
    //         var data = e.data;
    //         $rootScope.$apply(function () {
    //           if (callback) {
    //             callback.apply(socket, [data]);
    //           }
    //         });
    //       }
    //     },
    //
    //     onerror: function (callback) {
    //       socket.onerror = function (error) {
    //         $rootScope.$apply(function () {
    //           if (callback) {
    //             callback.apply(socket, [error]);
    //           }
    //         });
    //       };
    //     },
    //
    //     send: function (message) {
    //         message.senderDeviceName = new Fingerprint().get();
    //         socket.send(JSON.stringify(message));
    //     }
    // }
});
