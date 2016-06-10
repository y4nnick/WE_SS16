angular.module('App')
    .config(function($stateProvider,$urlRouterProvider/*,$resourceProvider*/) {

        $stateProvider
            .state('login',         {url: '/login',         templateUrl: 'src/main/webapp/views/login.html',          controller:'loginController'})
            .state('registration',  {url: '/registration',  templateUrl: 'src/main/webapp/views/registration.html',    controller:'registrationController'})
            .state('overview',      {url: '/overview',      templateUrl: 'src/main/webapp/views/overview.html',         controller:'overviewController'})
            .state('details',       {url: '/details',       templateUrl: 'src/main/webapp/views/details.html',    controller:'detailsController'});

        $urlRouterProvider.otherwise("/login");
       // $resourceProvider.defaults.stripTrailingSlashes = false;
    });