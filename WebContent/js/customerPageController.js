app.controller('customerPageController', function($http, $scope, $rootScope) {

	$scope.books = null;
	
	$scope.reviews = null;
	
	$scope.customer = $rootScope.customer;
	
	$scope.customers = $rootScope.customers;
	
	$http.get("reviews").success(function(data, status, headers, config) {
		$scope.reviews = data;
	});

	$scope.getPhoto = function()
	{
		return "Images/"+$scope.customer.username+".jpg;"
	}
	
	var getResult = $http.get("books");
	
	getResult.success(function(data, status, headers, config) {
		$scope.books = data;
	});
	
	$scope.getReview = function(book){
		var review ="";
		angular.forEach($scope.reviews, function(r){
			if(r.reviewerUsername == $scope.customer.username && r.bookName == book.bookName && r.isApproved > 0){
				review = r.review;
			}
		})
		return review;
	}
	
	$scope.getReviewDate = function(book){
		var date="";
		angular.forEach($scope.reviews, function(r){
			if(r.reviewerUsername == $scope.customer.username && r.bookName == book.bookName && r.isApproved > 0){
				date = r.date;
			}
		})
		return date;
	}
	
	$scope.hasBook = function(book) {
		
		var res = $.inArray(book.bookName, $scope.customer.myBookList);
		
		return res;
	}
	
	$scope.isLiked = function(book) {
		return $.inArray($scope.customer.username, book.likes);
	}

	$scope.deleteCustomer = function() {
		
		var res = $http.delete('customers/'+$scope.customer.username);
		
		res.success(function(data, status, headers, config) {
			var index = $scope.customers.indexOf($scope.customer);
			$scope.customers.splice(index, 1);
			$rootScope.rootAdminPath = "HTML/usersPage.html";
		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))

		});
		
	}

});