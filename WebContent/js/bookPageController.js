app
		.controller(
				'bookPageController',
				function($rootScope, $scope, $http) {
					
					$('[data-toggle="tooltip"]').tooltip();

					$scope.book = $rootScope.book;

					$scope.bookPageBoxPath = "HTML/bookDescription.html";

					$scope.getReviewImage = function(review) {
						return "Images/" + review.reviewerUsername + ".jpg";
					}

					$scope.goToReadBook = function() {
						$rootScope.book = $scope.book;
						$rootScope.path = "HTML/readBook.html";
					}

					$http.get("reviews").success(
							function(data, status, headers, config) {
								$scope.reviews = data;
							});

					$scope.showDescription = function() {
						$scope.bookPageBoxPath = "HTML/bookDescription.html";
					}

					$scope.showReviews = function() {
						$scope.bookPageBoxPath = "HTML/reviews.html";
					}

					$scope.addReview = function() {
						$scope.bookPageBoxPath = "HTML/addReview.html";
					}

					$scope.hasBook = function() {

						if ($rootScope.rootLogedUser.affiliation == "Admin") {
							return 1;
						}

						return $.inArray($scope.book.bookName,
								$rootScope.rootLogedUser.myBookList);
					}

					$scope.isOnCart = function(book) {
						var result = 0;
						angular.forEach($rootScope.rootCartBooks, function(b) {
							if (b.bookName == book.bookName) {
								result = 1;
							}
						});
						return result;
					}

					$scope.addToCart = function() {
						$rootScope.rootCartBooks.push($scope.book)
					}

					$scope.buyNow = function() {
						$rootScope.rootBuyNow.push($scope.book)
						$rootScope.path = "HTML/payment.html";
					}

					$scope.removeFromCart = function(book) {
						var index;
						for (var i = 0; i < $rootScope.rootCartBooks.length; i++) {
							if ($rootScope.rootCartBooks[i].bookName == book.bookName) {
								index = i;
							}
						}
						$rootScope.rootCartBooks.splice(index, 1);
					}

					$scope.isLiked = function() {

						return $.inArray($rootScope.rootLogedUser.username, $scope.book.likes);
					}

					$scope.doLike = function() {

						var likeRequest = {
							isLike : true,
							bookName : $scope.book.bookName,
							username : $rootScope.rootLogedUser.username
						}

						var res = $http.put("books", likeRequest);
						res.success(function(data, status, headers, config) {
							$scope.book.likes.push($rootScope.rootLogedUser.username);
						});
					}

					$scope.doUnLike = function() {

						var likeRequest = {
							isLike : false,
							bookName : $scope.book.bookName,
							username : $rootScope.rootLogedUser.username
						}

						var res = $http.put("books", likeRequest);
						
						res.success(function(data, status, headers, config) {
							var index = $scope.book.likes.indexOf($rootScope.rootLogedUser.username);
							$scope.book.likes.splice(index, 1);
						});
					}
				});