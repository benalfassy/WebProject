// create the module and name it app
var app = angular.module('app', [ 'txtHighlight' ]);

app.controller('appController', function($http, $scope, $rootScope) {

	$rootScope.path = "HTML/loginForm.html";

	$rootScope.rootNavPath = "HTML/NavBars/homeNavBar.html";

	$rootScope.rootLogedUser = null;

	$scope.logedUser = null;

	$scope.navPath = $rootScope.rootNavPath;

	$scope.filePath = $rootScope.path;
	
	$http.get("books").success(function(data, status, headers, config) {
		$scope.books = data;
	});

	$scope.nav = function(p) {
		$rootScope.path = p;
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

app.controller('loginController', function($http, $scope, $rootScope) {

	$scope.credentialError = false;

	$scope.submit = function() {

	var res = $http.get('customers/' + $scope.username);

	res.success(function(data, status, headers, config) {
	if(data.password==$scope.password){
		if (data.affiliation == "Admin") {
			$rootScope.path = "HTML/customersTable.html";
		} else {
			$rootScope.path = "HTML/userHome.html";
			$rootScope.rootNavPath = "HTML/NavBars/userNavBar.html";
		}
			$rootScope.rootLogedUser = data;
	}
	else
		{
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

app.controller('booksController', function($rootScope, $scope, $http) {
	
	$http.get("books").success(function(data, status, headers, config) {
		$scope.books = data;
	});
	
	$scope.goToBookPage = function(book) {
		$rootScope.book = book;
		$rootScope.path = "HTML/bookPage.html";
	}

	$scope.hasBook = function(book) {
		var res = $.inArray(book.bookName, $rootScope.rootLogedUser.myBookList);
		return res;
	}
	
	$scope.isLiked = function(book) {
		return $.inArray($rootScope.rootLogedUser.username, book.likesList);
	}
	
	$scope.doLike = function(book){
		
		var likeRequest = 
			{
				isLike : true,
				bookName : book.bookName,
				username : $rootScope.rootLogedUser.username
			}
		
		$http.put("books",likeRequest).success(function(data, status, headers, config) {
			var index = $scope.books.indexOf(book);
			$scope.books[index].likesList.push($rootScope.rootLogedUser.username);
			$scope.books[index].likes+=","+$rootScope.rootLogedUser.username;
		});
	}
	
	$scope.doUnLike = function(book){
		
		var likeRequest = 
		{
			isLike : false,
			bookName : book.bookName,
			username : $rootScope.rootLogedUser.username
		}
		
		$http.put("books",likeRequest).success(function(data, status, headers, config) {
			var index = $scope.books.indexOf(book);
			$scope.books[index].likesList.pop($rootScope.rootLogedUser.username);
			if( $scope.books[index].likes.indexOf(","+$rootScope.rootLogedUser.username)!=-1)
				$scope.books[index].likes=$scope.books[index].likes.replace(","+$rootScope.rootLogedUser.username, "");
			else if( $scope.books[index].likes.indexOf($rootScope.rootLogedUser.username+",")!=-1)
				$scope.books[index].likes=$scope.books[index].likes.replace($rootScope.rootLogedUser.username+",", "");
			else
				$scope.books[index].likes=$scope.books[index].likes.replace($rootScope.rootLogedUser.username, "");		
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

app.controller('bookPageController', function($rootScope, $scope, $http) {

	$scope.book = $rootScope.book;

	$scope.bookPageBoxPath = "HTML/bookDescription.html";
	
	$scope.imgSource = $rootScope.rootLogedUser.photo;

	$http.get("reviews").success(function(data, status, headers, config) {
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
		return $.inArray($scope.book.bookName, $rootScope.rootLogedUser.myBookList);
	}
	
	$scope.isLiked = function() {
		return $.inArray($rootScope.rootLogedUser.username, $scope.book.likesList);
	}
	
	$scope.doLike = function(){
		
		var likeRequest = 
		{
			isLike : true,
			bookName : $scope.book.bookName,
			username : $rootScope.rootLogedUser.username
		}
		
		$http.put("books",likeRequest).success(function(data, status, headers, config) {
			$scope.book.likesList.push($rootScope.rootLogedUser.username);
			$scope.book.likes+=","+$rootScope.rootLogedUser.username;
		});
	}
	
	$scope.doUnLike = function(){
		
		var likeRequest = 
		{
			isLike : false,
			bookName : $scope.book.bookName,
			username : $rootScope.rootLogedUser.username
		}
		
		$http.put("books",likeRequest).success(function(data, status, headers, config) {
			$scope.book.likesList.pop($rootScope.rootLogedUser.username);
			if( $scope.book.likes.indexOf(","+$rootScope.rootLogedUser.username)!=-1)
				$scope.book.likes=$scope.book.likes.replace(","+$rootScope.rootLogedUser.username, "");
			else if( $scope.book.likes.indexOf($rootScope.rootLogedUser.username+",")!=-1)
				$scope.book.likes=$scope.book.likes.replace($rootScope.rootLogedUser.username+",", "");
			else
				$scope.book.likes=$scope.book.likes.replace($rootScope.rootLogedUser.username, "");			
		});
	}

});

app.controller('userProfileController', function($http, $scope, $rootScope) {
	$scope.user = $rootScope.rootLogedUser;
	$scope.userProfilePath = "HTML/myDetails.html";
	
	$scope.changeUserProfilePath = function(path)
	{
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
			myBooks : null
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

app.controller('inTableSearchController', [
		'$scope',
		'$http',
		'highlightText',
		function($scope, $http, highlightText) {

			$scope.query = "";// this variable will hold the user's

			// query

			// obtain some dataset online
			// $http is AngularJS way to do ajax-like communications
			$http.get("customers") // /name/Alfreds
			// Futterkiste
			.success(function(response) {
				$scope.records = response;
				$scope.result = $scope.records;// this variable will
				// hold the search
				// results
			});

			// this method will be called upon change in the text typed
			// by the user in the searchbox
			$scope.search = function() {
				if (!$scope.query || $scope.query.length == 0) {
					// initially we show all table data
					$scope.result = $scope.records;
				} else {
					var qstr = $scope.query.toLowerCase();
					$scope.result = [];
					for (x in $scope.records) {
						// check for a match (up to a lowercasing
						// difference)
						if ($scope.records[x].username.toLowerCase()
								.match(qstr)
								|| $scope.records[x].email.toLowerCase().match(
										qstr)
								|| $scope.records[x].affiliation.toLowerCase()
										.match(qstr)) {
							$scope.result.push($scope.records[x]); // add
							// record
							// to
							// search
							// result
						}
					}
				}
			};

			// delegate the text highlighting task to an external helper
			// service
			$scope.hlight = function(text, qstr) {
				return highlightText.highlight(text, qstr);
			};

		} ]);
