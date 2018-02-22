//controller for the book page 

app
		.controller(
				'bookPageController',
				function($rootScope, $scope, $http) {
					
					$('[data-toggle="tooltip"]').tooltip();

					$scope.book = $rootScope.book;
					
					$("#"+$scope.book.bookName).mouseover(function(){
						$("#"+$scope.book.bookName+"-likesContent").show();
					});
					
				    $("#"+$scope.book.bookName).mouseout(function(){
				        $("#"+$scope.book.bookName+"-likesContent").hide();
				    });

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

						return $.inArray($rootScope.rootLogedUser.nickName, $scope.book.likes);
					}

					$scope.doLike = function() {

						var likeRequest = {
							isLike : true,
							bookName : $scope.book.bookName,
							nickName : $rootScope.rootLogedUser.nickName
						}

						var res = $http.put("books", likeRequest);
						res.success(function(data, status, headers, config) {
							$scope.book.likes.push($rootScope.rootLogedUser.nickName);
						});
					}

					$scope.doUnLike = function() {

						var likeRequest = {
							isLike : false,
							bookName : $scope.book.bookName,
							nickName : $rootScope.rootLogedUser.nickName
						}

						var res = $http.put("books", likeRequest);
						
						res.success(function(data, status, headers, config) {
							var index = $scope.book.likes.indexOf($rootScope.rootLogedUser.nickName);
							$scope.book.likes.splice(index, 1);
						});
					}
					//function for the admin that allow him to delete a review from DB
					$scope.deleteReview = function(review){
						
						var res = $http.delete('reviews/'+review.reviewId);
						
						res.success(function(data, status, headers, config) {
							
							var index = $scope.reviews.indexOf(review);
							$scope.reviews.splice(index, 1);
							
						});
						
						res.error(function(data, status, headers, config) {
							
							alert("failure message: " + JSON.stringify({
								data : data
							}));
							
						});
					}
					//function for the admin - if click on likes tooltip on a user, set page to the user page
					
					$scope.goToCustomerPage = function(nickName){
						
						$http.get('customers').success(function(data, status, headers, config) {
							
							$rootScope.customers = data;
							
							angular.forEach(data,function(customer){
								if(customer.nickName == nickName){
									$rootScope.customer = customer;
								}
							})
							$rootScope.path = "HTML/customerPage.html";

						});
						

					}
				});