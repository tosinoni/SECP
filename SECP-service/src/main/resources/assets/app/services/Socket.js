'use strict';

angular.module('SECP')
  .factory('Socket', function($rootScope, $location) {
    let userID = localStorage.getItem('userID');
    let host = location.host;
    let protocol = "wss";

    if($location.protocol() === "http") {
        protocol = "ws";
    }

    var socket = new WebSocket(protocol + "://" + host + "/chat/" + userID);
    //var socket = new WebSocket("ws://localhost:8080/chat/" + userID);



      return {
        onopen: function (callback) {
          socket.onopen = function () {
            $rootScope.$apply(function () {
              callback.apply(socket);
            });
          };
        },

        onmessage: function (callback) {
          socket.onmessage = function (e) {
            var data = e.data;
            $rootScope.$apply(function () {
              if (callback) {
                callback.apply(socket, [data]);
              }
            });
          }
        },

        onerror: function (callback) {
          socket.onerror = function (error) {
            $rootScope.$apply(function () {
              if (callback) {
                callback.apply(socket, [error]);
              }
            });
          };
        },

        send: function (message) {
            message.senderDeviceName = new Fingerprint().get();
            socket.send(JSON.stringify(message));
        }
    }
});
