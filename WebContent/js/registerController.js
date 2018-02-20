app.controller('registerController', function($http, $scope, $rootScope) {

	$scope.alreadyExists = false;

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
			affiliation : "User",
			myBooks : "",
			myBookList : [],
			bookScroll : []
		}

		var res = $http.post('customers', customer);
		res.success(function(data, status, headers, config) {
			
			var message = {
					from: "BooksForAll",
					to: customer.username,
					content: "Welcome to Books For All! We hope you enjoy!!!"
			}
			$http.post('messages', message).success(function(data, status, headers, config) {
				$http.get('messages').success(function(data, status, headers, config) {
					$rootScope.rootMessages = data;
				});
			});
			
			$rootScope.rootLogedUser = customer;
			$rootScope.rootNavPath = "HTML/NavBars/userNavBar.html";
			$rootScope.path = "HTML/userHome.html";
		});
		res.error(function(data, status, headers, config) {

			if (status == 409) {
				$scope.alreadyExists = true;
			} else {
				alert("failure message: " + JSON.stringify({
					data : data
				}));
			}

		});
	}
});