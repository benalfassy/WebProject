// create the module and name it app
var app = angular.module('app', [ 'txtHighlight' ]);

app.factory('pathFactory', function() {

});

app.controller('appController', function($scope, $rootScope) {

	$rootScope.path = "HTML/loginForm.html";

	$scope.filePath = $rootScope.path;

	$scope.nav = function(p) {
		$rootScope.path = p;
	}

	$scope.$watch(function() {
		return $rootScope.path;
	}, function() {
		$scope.filePath = $rootScope.path;
	}, true);

});

app.controller('loginController', function($scope, $rootScope) {
	
	$scope.credentialError = false;

	$scope.submit = function() {
		if ($scope.username == "Admin") {
			$rootScope.path = "HTML/customersTable.html";
		}
		else{
			$scope.credentialError = true;
		}
	}
});

app.controller('homeController', function($scope) {

});

app.controller('registerController', function($http, $scope, $rootScope) {
	
	$scope.submit = function() {
		var customer = {
			username : $scope.username,
			email : $scope.email,
			street : $scope.street,
			streetNum : $scope.streetNo,
			city : $scope.city,
			zipCode : $scope.zipCode,
			phoneNum : $scope.phoneNum,
			password : $scope.password,
			nickName : $scope.nickName,
			description : $scope.description,
			photo : $scope.photo,
			affiliation : "User"
		}

		var res = $http.post('customers', customer);
		res.success(function(data, status, headers, config) {
			$rootScope.path = "HTML/Home.html";
		});
		res.error(function(data, status, headers, config) {
			alert("failure message: " + JSON.stringify({
				data : data
			}));
		});
	}
});

app.controller('inTableSearchController', [
		'$scope',
		'$http',
		'highlightText',
		function($scope, $http, highlightText) {

			$scope.query = "";// this variable will hold the user's

			// query

			// obtain some dataset online
			// $http is AngularJS way to do ajax-like communications
			$http.get("customers") // /name/Alfreds
			// Futterkiste
			.success(function(response) {
				$scope.records = response;
				$scope.result = $scope.records;// this variable will
				// hold the search
				// results
			});

			// this method will be called upon change in the text typed
			// by the user in the searchbox
			$scope.search = function() {
				if (!$scope.query || $scope.query.length == 0) {
					// initially we show all table data
					$scope.result = $scope.records;
				} else {
					var qstr = $scope.query.toLowerCase();
					$scope.result = [];
					for (x in $scope.records) {
						// check for a match (up to a lowercasing
						// difference)
						if ($scope.records[x].username.toLowerCase()
								.match(qstr)
								|| $scope.records[x].email.toLowerCase().match(
										qstr)
								|| $scope.records[x].affiliation.toLowerCase()
										.match(qstr)) {
							$scope.result.push($scope.records[x]); // add
							// record
							// to
							// search
							// result
						}
					}
				}
			};

			// delegate the text highlighting task to an external helper
			// service
			$scope.hlight = function(text, qstr) {
				return highlightText.highlight(text, qstr);
			};

		} ]);
