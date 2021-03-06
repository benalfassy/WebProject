//controller for the login page

app.controller('loginController', function($http, $scope, $rootScope) {
	$rootScope.rootLogedUser = null;

	$scope.credentialError = false;

	$scope.submit = function() {

		var res = $http.get('customers/' + $scope.username);
		
		//validate the username and password
		res.success(function(data, status, headers, config) {
			if (data.password == $scope.password) {
				if (data.affiliation == "Admin") {
					$rootScope.path = "HTML/userHome.html";
					$rootScope.rootAdminPath = "HTML/userHome.html";
					$rootScope.rootNavPath = "HTML/NavBars/adminUpBar.html";
				} else {
					$rootScope.path = "HTML/userHome.html";
					$rootScope.rootNavPath = "HTML/NavBars/userNavBar.html";
				}
				$rootScope.rootLogedUser = data;
			} else {
				$scope.credentialError = true;
			}
		});

		res.error(function(data, status, headers, config) {

			if (status == 404) {
				$scope.credentialError = true;
			} else {
				alert("failure message: " + JSON.stringify({
					data : data
				}))
			}
		});
	}
});