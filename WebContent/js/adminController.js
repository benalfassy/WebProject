app.controller('adminController', function($http, $scope, $rootScope) {

	$scope.reviews = null;
	$rootScope.rootReviews = null;
	
	$http.get("reviews").success(function(data, status, headers, config) {
		$rootScope.rootReviews = data;
	});
	
	$scope.messagesNavDisplay = function() {

		var newMessages = 0;

		angular.forEach($scope.messages, function(m) {
			if (m.to == "Admin" && m.isViewed == 0) {
				newMessages++;
			}
		})

		if (newMessages > 0) {
			return "(" + newMessages + ")";
		} else {
			return "";
		}
	}
	
	$scope.reviewsNavDisplay = function() {

		var reviewsNum = 0;

		angular.forEach($scope.reviews, function(r) {
			if (r.isApproved == 0) {
				reviewsNum++;
			}
		})

		if (reviewsNum > 0) {
			return "(" + reviewsNum + ")";
		} else {
			return "";
		}
	}
	
	$scope.totalMessagesNavDisplay = function(){
		var total = 0;
		
		angular.forEach($scope.reviews, function(r) {
			if (r.isApproved == 0) {
				total++;
			}
		})
		
		angular.forEach($scope.messages, function(m) {
			if (m.to == "Admin" && m.isViewed == 0) {
				total++;
			}
		})
		
		if (total > 0) {
			return "(" + total + ")";
		} else {
			return "";
		}
	}
	
	$scope.updateMessagesStatus = function() {
		angular.forEach($scope.messages, function(m) {
			if (m.to == $rootScope.rootLogedUser.username && m.isViewed == 0) {
				var message = {
					messageId : m.messageId,
					from : m.from,
					to : m.to,
					content : m.content,
					date : m.date,
					isViewed : 1
				}
				$http.put('messages', message).success(
						function(data, status, headers, config) {
							m.isViewed = 1;
						});
			}
		});
	}
	
	$scope.$watch(function() {
		return $rootScope.rootReviews;
	}, function() {
		$scope.reviews = $rootScope.rootReviews;
	}, true);

});

