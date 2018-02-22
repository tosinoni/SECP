angular.module('SECP')
  .controller('HowToController', ['$scope', function ($scope) {
      $(document).ready(function(){
          // Toggle plus minus icon on show hide of collapse element
          $(".collapse").on('show.bs.collapse', function(){
              $(this).parent().find(".glyphicon").removeClass("glyphicon-plus").addClass("fa fa-minus");
          }).on('hide.bs.collapse', function(){
              $(this).parent().find(".glyphicon").removeClass("fa fa-minus").addClass("glyphicon-plus");
          });
      });
  }]);
