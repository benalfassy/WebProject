//Controller for the displayed content of the book

app.controller('readBookController', function($log, $window, $http, $scope,
		$rootScope) {

	$scope.book = $rootScope.book;

	$scope.goToBookPage = function() {
		$rootScope.path = "HTML/bookPage.html";
	}

	$scope.goToLastScroll = function() {
		var pairList = $rootScope.rootLogedUser.bookScroll;

		angular.forEach(pairList, function(pair) {
			if (pair.key == $scope.book.bookName) {
				$window.scrollTo(0, pair.value);
			}
		});
	}

	$window.onbeforeunload = function(event) {
		saveScroll($window.pageYOffset)
	}

	$scope.$on("$destroy", function() {
		saveScroll($window.pageYOffset)
	})

	var saveScroll = function(scroll) {
		var customer = $rootScope.rootLogedUser;

		angular.forEach(customer.bookScroll, function(pair) {
			if (pair.key == $scope.book.bookName) {
				pair.value = scroll;
			}
		});

		var res = $http.put("customers", customer);

		res.error(function(data, status, headers, config) {
			// retry
			$http.put("customers", customer);
		});

		$rootScope.rootLogedUser = customer;
	}

});