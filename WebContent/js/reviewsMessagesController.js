app.controller('reviewsMessagesController', function($rootScope, $scope, $http) {

	$scope.reviews = null;
	
	var getReviews = $http.get("reviews");

	getReviews.success(function(data, status, headers, config) {
		$scope.reviews = data;
	});
	
	$scope.approveReview = function(review){
		
	}
	
	$scope.rejectReview = function(review){
		
	}

});
