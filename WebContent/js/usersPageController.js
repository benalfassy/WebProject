app.controller('usersPageController', function($http, $scope, $rootScope) {

	$scope.customers = null;

	var res = $http.get('customers');

	res.success(function(data, status, headers, config) {
		$scope.customers = data;
	});

	res.error(function(data, status, headers, config) {

		alert("failure message: " + JSON.stringify({
			data : data
		}))

	});
	
	$scope.goToCustomerPage = function(customer) {
		$rootScope.customer = customer;
		$rootScope.customers = $scope.customers;
		$rootScope.rootAdminPath = "HTML/customerPage.html";
	}
	
	$scope.getPhoto = function(customer)
	{
		return "Images/"+customer.username+".jpg;"
	}

	$scope.deleteCustomer = function(customer) {
		
		var res = $http.delete('customers/'+customer.username);
		
		res.success(function(data, status, headers, config) {
			var index = $scope.customers.indexOf(customer);
			$scope.customers.splice(index, 1);
		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))

		});
		
	}

});