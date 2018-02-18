// create the module and name it app

var app = angular.module('app', []);

app.controller('appController', function($http, $scope, $rootScope) {

	$rootScope.path = "HTML/loginForm.html";

	$rootScope.rootAdminPath = "HTML/userHome.html";

	$rootScope.rootNavPath = "HTML/NavBars/homeNavBar.html";

	$rootScope.rootLogedUser = null;

	$scope.logedUser = null;

	$rootScope.rootCartBooks = [];
	$rootScope.rootBuyNow = [];

	$scope.navPath = $rootScope.rootNavPath;

	$scope.filePath = $rootScope.path;

	$scope.adminPath = $rootScope.rootAdminPath;

	$http.get("books").success(function(data, status, headers, config) {
		$scope.books = data;
	});

	$scope.isAdmin = function() {
		return $rootScope.rootLogedUser.affiliation == "Admin";
	}

	$scope.cartNavDisplay = function() {
		if ($rootScope.rootCartBooks.length > 0) {
			return "(" + $rootScope.rootCartBooks.length + ")";
		} else {
			return "";
		}
	}

	$scope.nav = function(p) {
		$rootScope.path = p;
	}

	$scope.adminNav = function(p) {
		$rootScope.rootAdminPath = p;
	}

	$scope.changeNavBar = function(n) {
		$rootScope.rootNavPath = n;
	}

	$scope.LogOut = function() {
		if (confirm("Are you sure you want to log-out?")) {
			$rootScope.rootNavPath = "HTML/NavBars/homeNavBar.html";
			$rootScope.path = "HTML/loginForm.html";
			$rootScope.rootLogedUser = null;
		}
	}

	$scope.$watch(function() {
		return $rootScope.path;
	}, function() {
		$scope.filePath = $rootScope.path;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootAdminPath;
	}, function() {
		$scope.adminPath = $rootScope.rootAdminPath;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootNavPath;
	}, function() {
		$scope.navPath = $rootScope.rootNavPath;
	}, true);

	$scope.$watch(function() {
		return $rootScope.rootLogedUser;
	}, function() {
		$scope.logedUser = $rootScope.rootLogedUser;
	}, true);

});


app.controller('usersController', function($http, $scope, $rootScope) {
	
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

});

app.controller('loginController', function($http, $scope, $rootScope) {

	$scope.credentialError = false;

	$scope.submit = function() {

		var res = $http.get('customers/' + $scope.username);

		res.success(function(data, status, headers, config) {
			if (data.password == $scope.password) {
				if (data.affiliation == "Admin") {
					$rootScope.path = "HTML/Admin.html";
					$rootScope.rootNavPath = null;
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

app.controller('addReviewController', function($rootScope, $scope, $http) {

	$scope.submitSucceed = false;

	$scope.submitReview = function() {

		var reviewObj = {
			bookName : $rootScope.book.bookName,
			review : $scope.review,
			reviewerUsername : $rootScope.rootLogedUser.username
		}

		var res = $http.post('reviews', reviewObj);

		res.success(function(data, status, headers, config) {
			$scope.submitSucceed = true;
		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}));

		});
	}
});

app
		.controller(
				'booksController',
				function($rootScope, $scope, $http) {

					$http.get("books").success(
							function(data, status, headers, config) {
								$scope.books = data;
							});

					$scope.goToBookPage = function(book) {
						$rootScope.book = book;
						$rootScope.path = "HTML/bookPage.html";
					}

					$scope.goToReadBook = function(book) {
						$rootScope.book = book;
						$rootScope.path = "HTML/readBook.html";
					}

					$scope.hasBook = function(book) {

						if ($rootScope.rootLogedUser.affiliation == "Admin") {
							return 1;
						}
						var res = $.inArray(book.bookName,
								$rootScope.rootLogedUser.myBookList);
						return res;
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

					$scope.addToCart = function(book) {
						$rootScope.rootCartBooks.push(book)
					}

					$scope.buyNow = function(book) {
						$rootScope.rootBuyNow.push(book)
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

					$scope.isLiked = function(book) {
						return $.inArray($rootScope.rootLogedUser.nickName,
								book.likesList);
					}

					$scope.doLike = function(book) {

						var likeRequest = {
							isLike : true,
							bookName : book.bookName,
							username : $rootScope.rootLogedUser.nickName
						}

						$http
								.put("books", likeRequest)
								.success(
										function(data, status, headers, config) {
											var index = $scope.books
													.indexOf(book);
											$scope.books[index].likesList
													.push($rootScope.rootLogedUser.nickName);
											$scope.books[index].likes += ","
													+ $rootScope.rootLogedUser.nickName;
										});
					}

					$scope.doUnLike = function(book) {

						var likeRequest = {
							isLike : false,
							bookName : book.bookName,
							username : $rootScope.rootLogedUser.nickName
						}

						$http
								.put("books", likeRequest)
								.success(
										function(data, status, headers, config) {
											var index = $scope.books
													.indexOf(book);
											$scope.books[index].likesList
													.pop($rootScope.rootLogedUser.nickName);
											if ($scope.books[index].likes
													.indexOf(","
															+ $rootScope.rootLogedUser.nickName) != -1)
												$scope.books[index].likes = $scope.books[index].likes
														.replace(
																","
																		+ $rootScope.rootLogedUser.nickName,
																"");
											else if ($scope.books[index].likes
													.indexOf($rootScope.rootLogedUser.nickName
															+ ",") != -1)
												$scope.books[index].likes = $scope.books[index].likes
														.replace(
																$rootScope.rootLogedUser.nickName
																		+ ",",
																"");
											else
												$scope.books[index].likes = $scope.books[index].likes
														.replace(
																$rootScope.rootLogedUser.nickName,
																"");
										});
					}

					$scope.shortDescription = function(description) {
						var count = 0;
						var i = 0;
						var result = '';
						while (count < 15 && description[i] != undefined) {
							if (description[i] == ' ') {
								count++;
							}
							result += description[i];
							i++;
						}
						result += '...';
						return result;
					}
				});

app
		.controller(
				'bookPageController',
				function($rootScope, $scope, $http) {

					$scope.book = $rootScope.book;

					$scope.bookPageBoxPath = "HTML/bookDescription.html";

					$scope.imgSource = $rootScope.rootLogedUser.photo;

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

						if ($rootScope.rootLogedUser.affiliation == "Admin") {
							return 1;
						}
					}

					$scope.doLike = function() {

						var likeRequest = {
							isLike : true,
							bookName : $scope.book.bookName,
							username : $rootScope.rootLogedUser.nickName
						}

						$http
								.put("books", likeRequest)
								.success(
										function(data, status, headers, config) {
											$scope.book.likesList
													.push($rootScope.rootLogedUser.nickName);
											$scope.book.likes += ","
													+ $rootScope.rootLogedUser.nickName;
										});
					}

					$scope.doUnLike = function() {

						var likeRequest = {
							isLike : false,
							bookName : $scope.book.bookName,
							username : $rootScope.rootLogedUser.nickName
						}

						$http
								.put("books", likeRequest)
								.success(
										function(data, status, headers, config) {
											$scope.book.likesList
													.pop($rootScope.rootLogedUser.nickName);
											if ($scope.book.likes
													.indexOf(","
															+ $rootScope.rootLogedUser.nickName) != -1)
												$scope.book.likes = $scope.book.likes
														.replace(
																","
																		+ $rootScope.rootLogedUser.nickName,
																"");
											else if ($scope.book.likes
													.indexOf($rootScope.rootLogedUser.nickName
															+ ",") != -1)
												$scope.book.likes = $scope.book.likes
														.replace(
																$rootScope.rootLogedUser.nickName
																		+ ",",
																"");
											else
												$scope.book.likes = $scope.book.likes
														.replace(
																$rootScope.rootLogedUser.nickName,
																"");
										});
					}

				});

app.controller('userProfileController', function($http, $scope, $rootScope) {
	$scope.user = $rootScope.rootLogedUser;
	$scope.userProfilePath = "HTML/myDetails.html";

	$scope.changeUserProfilePath = function(path) {
		$scope.userProfilePath = path;
	}
});

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
			myBooks : ""
		}

		var res = $http.post('customers', customer);
		res.success(function(data, status, headers, config) {
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

app.controller('readBookController', function($log, $window, $http, $scope,
		$rootScope) {

	$scope.book = $rootScope.book;

	$scope.goToBookPage = function() {
		$rootScope.path = "HTML/bookPage.html";
	}

	$scope.goToLastScroll = function() {
		var pairList = $rootScope.rootLogedUser.bookScroll;

		angular.forEach(pairList, function(pair) {
			if (pair.key == $scope.book.bookName) {
				$window.scrollTo(0, pair.value);
			}
		});
	}

	$window.onbeforeunload = function(event) {
		saveScroll($window.pageYOffset)
	}

	$scope.$on("$destroy", function() {
		saveScroll($window.pageYOffset)
	})

	var saveScroll = function(scroll) {
		var customer = $rootScope.rootLogedUser;

		angular.forEach(customer.bookScroll, function(pair) {
			if (pair.key == $scope.book.bookName) {
				pair.value = scroll;
			}
		});

		var res = $http.put("customers", customer);

		res.error(function(data, status, headers, config) {
			// retry
			$http.put("customers", customer);
		});

		$rootScope.rootLogedUser = customer;
	}

});

app.controller('paymentController', function($http, $scope, $rootScope) {

	if ($rootScope.rootBuyNow.length > 0) {
		$scope.cartBooks = $rootScope.rootBuyNow;
	} else {
		$scope.cartBooks = $rootScope.rootCartBooks;
	}

	$scope.$on("$destroy", function() {
		$rootScope.rootBuyNow.pop();
	});

	$scope.amountToPay = function() {
		var sum = 0;

		angular.forEach($scope.cartBooks, function(b) {
			sum += b.price;
		})
		return sum;
	}

	$scope.checkExpiryDate = function() {

		var today = new Date();

		var year = "20" + $scope.expiryDate[3] + $scope.expiryDate[4];

		var month = $scope.expiryDate[0] + $scope.expiryDate[1];

		var checkedDate = new Date(year, month);

		return checkedDate > today;
	}

	$scope.onBuy = function() {
		var customer = $rootScope.rootLogedUser;

		if (customer.myBooks != "") {
			customer.myBooks += ",";
		}
		angular.forEach($scope.cartBooks, function(b) {

			customer.myBooks += b.bookName + ",";

			customer.myBookList.push(b.bookName);

			var pair = {
				key : b.bookName,
				value : "0"
			}

			customer.bookScroll.push(pair);
		});
		customer.myBooks += "$";
		customer.myBooks = customer.myBooks.replace(",$", "");

		var res = $http.put("customers", customer);

		res.success(function(data, status, headers, config) {

			$rootScope.rootLogedUser = customer;

			if ($rootScope.rootBuyNow.length == 0) {
				$rootScope.rootCartBooks = [];
			}

			$rootScope.path = "HTML/userHome.html";

		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))

		});
	}

});

app.controller('shoppingCartController', function($http, $scope, $rootScope) {

	$scope.cartBooks = $rootScope.rootCartBooks;

	$scope.amountToPay = function() {
		var sum = 0;

		angular.forEach($scope.cartBooks, function(book) {
			sum += book.price;
		});

		return sum;
	}

	$scope.isCartEmpty = function() {
		return $scope.cartBooks.length == 0;
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
});
