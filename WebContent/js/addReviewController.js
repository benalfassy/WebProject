//Controller for the add review section in the book page for users that purchased the book

app.controller('addReviewController', function($rootScope, $scope, $http) {

	$scope.submitSucceed = false;
	
	//function that add the review to the DB
	$scope.submitReview = function() {

		var reviewObj = {
			bookName : $rootScope.book.bookName,
			review : $scope.review,
			reviewerUsername : $rootScope.rootLogedUser.username
		}

		var res = $http.post('reviews', reviewObj);

		res.success(function(data, status, headers, config) {
			$scope.submitSucceed = true;
			$scope.review = "";
		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}));

		});
	}
});
