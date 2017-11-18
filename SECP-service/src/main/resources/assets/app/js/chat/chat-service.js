'use strict';

angular.module('SECP')
  .factory('Chat', function() {
    // factory returns an object
    // you can run some code before

    return {
        getMessages : function() {
             var messages = [
                {
                  'id' : '1',
                  'username': 'Yanique Robinson',
                  'time': '9 mins ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar1.png',
                  'contents': [
                    {
                        'text' : "Hey, are you busy at the moment?"
                    }
                  ]
                },
                {
                  'id' : '2',
                  'username': 'Kevin Mckoy',
                  'time': '5 mins ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar2.png',
                  'contents': [
                    {
                        'text' : "Um, no actually. I've just wrapped up my last project for the day"
                    },
                    {
                        'text' : "Whats up?"
                    }
                  ]
                },
                {
                  'id' : '3',
                  'username': 'Yanique Robinson',
                  'time': '3 mins ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar1.png',
                  'contents': [
                    {
                        'text' : "Well, I wanted to find out if you have any plans for tonight"
                    },
                    {
                        'text' : "Barbara and I are going to this restaurant out of town.",
                        'imgSrc' : "http://lorempixel.com/300/200/nature/3"
                    }
                  ]
                },
                {
                  'id' : '4',
                  'username': 'Kevin Mckoy',
                  'time': '2 mins ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar2.png',
                  'contents': [
                    {
                        'text' : "Wow that sounds great."
                    }
                  ]
                },
                {
                  'id' : '5',
                  'username': 'Yanique Robinson',
                  'time': '56 secs ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar1.png',
                  'contents': [
                    {
                        'text' : "Ok! We'll swing by your office at 5"
                    }
                  ]
                },
                {
                  'id' : '6',
                  'username': 'Kevin Mckoy',
                  'time': '3 secs ago',
                  'imgSrc': 'http://bootdey.com/img/Content/avatar/avatar2.png',
                  'contents': [
                    {
                        'text' : "Cool. I'l see you guys then."
                    }
                  ]
                },
              ];
            return messages;
        },

        getCurrentUser : function() {
           var user = {
                'id' : '1',
                'username': 'Kevin Mckoy'
           };

           return user;
        }

    }
});
