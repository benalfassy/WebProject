var app = angular.module('app', []);

app.controller('appController', function($http, $scope, $rootScope) {

	$rootScope.path = "HTML/loginForm.html";

	$rootScope.rootAdminPath = "HTML/userHome.html";

	$rootScope.rootNavPath = "HTML/NavBars/homeNavBar.html";

	$rootScope.rootLogedUser = null;

	$scope.logedUser = null;

	$rootScope.customers = null;

	$rootScope.rootCartBooks = [];
	$rootScope.rootBuyNow = [];

	$scope.navPath = $rootScope.rootNavPath;

	$scope.filePath = $rootScope.path;

	$scope.adminPath = $rootScope.rootAdminPath;

	$scope.messages = null;
	
	$rootScope.rootMessages = null;

	$http.get('messages').success(function(data, status, headers, config) {
		$rootScope.rootMessages = data;
	});

	$http.get("books").success(function(data, status, headers, config) {
		$scope.books = data;
	});

	$scope.isAdmin = function() {
		return $rootScope.rootLogedUser.affiliation == "Admin";
	}

	$scope.cartNavDisplay = function() {
		if ($rootScope.rootCartBooks.length > 0) {
			return "(" + $rootScope.rootCartBooks.length + ")";
		} else {
			return "";
		}
	}
	
	$scope.usertNavDisplay = function() {
		
		var newMessages = 0;
		
		angular.forEach($scope.messages, function(m){
			if(m.to == $rootScope.rootLogedUser.username && m.isViewed == 0){
				newMessages ++;
			}
		})
		
		if (newMessages > 0) {
			return "(" + newMessages + ")";
		} else {
			return "";
		}
	}

	$scope.nav = function(p) {
		$rootScope.path = p;
	}

	$scope.adminNav = function(p) {
		$rootScope.rootAdminPath = p;
	}

	$scope.changeNavBar = function(n) {
		$rootScope.rootNavPath = n;
	}

	$scope.LogOut = function() {
		if (confirm("Are you sure you want to log-out?")) {
			$rootScope.rootNavPath = "HTML/NavBars/homeNavBar.html";
			$rootScope.path = "HTML/loginForm.html";
		}
	}
	
	$scope.$watch(function() {
		return $rootScope.rootMessages;
	}, function() {
		$scope.messages = $rootScope.rootMessages;
	}, true);

	$scope.$watch(function() {
		return $rootScope.path;
	}, function() {
		$scope.filePath = $rootScope.path;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootAdminPath;
	}, function() {
		$scope.adminPath = $rootScope.rootAdminPath;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootNavPath;
	}, function() {
		$scope.navPath = $rootScope.rootNavPath;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootLogedUser;
	}, function() {
		$scope.logedUser = $rootScope.rootLogedUser;
	}, true);

});
