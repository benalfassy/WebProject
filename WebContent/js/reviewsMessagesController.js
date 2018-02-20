app.controller('reviewsMessagesController', function($rootScope, $scope, $http) {
	
	$scope.approveReview = function(review){
		
		review.isApproved = 1;
		
		var res = $http.put('reviews',review);
		
		res.error(function(data, status, headers, config) {

			review.isApproved = 0;
			
			alert("failure message: " + JSON.stringify({
				data : data
			}));
			
		});
		
		var message = {
				from: "BooksForAll",
				to: review.reviewerUsername,
				content: "Your review for the book " + review.bookName + " was approved and now available on the book's page."
		}
		$http.post('messages', message).success(function(data, status, headers, config) {
			$http.get('messages').success(function(data, status, headers, config) {
				$rootScope.rootMessages = data;
			});
		});
	}
	
	$scope.rejectReview = function(review){
		
		var res = $http.delete('reviews/'+review.reviewId);
		
		res.success(function(data, status, headers, config) {
			
			var index = $rootScope.rootReviews.indexOf(review);
			$rootScope.rootReviews.splice(index, 1);
			
			var message = {
					from: "BooksForAll",
					to: review.reviewerUsername,
					content: "Your review for the book " + review.bookName + " was rejected. For more information, contact us in \"Help Me\". "
			}
			$http.post('messages', message).success(function(data, status, headers, config) {
				$http.get('messages').success(function(data, status, headers, config) {
					$rootScope.rootMessages = data;
				});
			});
			
		});
		
		res.error(function(data, status, headers, config) {
			
			alert("failure message: " + JSON.stringify({
				data : data
			}));
			
		});
	}

});
