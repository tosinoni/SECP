angular.module('SECP')
  .controller('HowToController', ['$scope', function ($scope) {
      $(document).ready(function(){
          // Add minus icon for collapse element which is open by default
          $(".collapse.in").each(function(){
              $(this).siblings(".panel-heading").find(".fa").addClass("fa-minus").removeClass("fa-plus");
          });

          // Toggle plus minus icon on show hide of collapse element
          $(".collapse").on('show.bs.collapse', function(){
              $(this).parent().find(".fa").removeClass("fa-plus").addClass("fa-minus");
          }).on('hide.bs.collapse', function(){
              $(this).parent().find(".fa").removeClass("fa-minus").addClass("fa-plus");
          });
      });
  }]);
