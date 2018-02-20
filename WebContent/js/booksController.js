app.controller('booksController',function($rootScope, $scope, $http) {
	
	$scope.filterNameValue = "";
	
	$scope.filterType = 0;
	
	$scope.filterMinValue = "";
	
	$scope.filterMaxValue = "";
	
	$scope.filterMaxLikeValue = "";
	
	$scope.filterMinLikeValue = "";

	var getResult = $http.get("books");
		
	getResult.success(function(data, status, headers, config) {
		$scope.books = data;
	});

	$scope.goToBookPage = function(book) {
		$rootScope.book = book;
		if($rootScope.rootLogedUser.affiliation == "Admin" ){
			$rootScope.rootAdminPath = "HTML/bookPage.html";
		}
		else{
			$rootScope.path = "HTML/bookPage.html";
		}
	}

	$scope.goToReadBook = function(book) {
		$rootScope.book = book;
		$rootScope.path = "HTML/readBook.html";
	}

	$scope.hasBook = function(book) {

		if ($rootScope.rootLogedUser.affiliation == "Admin") {
			return 1;
		}
		
		var res = $.inArray(book.bookName, $rootScope.rootLogedUser.myBookList);
		
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
		return $.inArray($rootScope.rootLogedUser.username, book.likes);
	}

	$scope.doLike = function(book) {

		var likeRequest = {
			isLike : true,
			bookName : book.bookName,
			username : $rootScope.rootLogedUser.username
		}

		var res = $http.put("books", likeRequest);
		res.success(function(data, status, headers, config) {
			book.likes.push($rootScope.rootLogedUser.username);
		});
	}

	$scope.doUnLike = function(book) {

		var likeRequest = {
			isLike : false,
			bookName : book.bookName,
			username : $rootScope.rootLogedUser.username
		}

		var res = $http.put("books", likeRequest);
		
		res.success(function(data, status, headers, config) {
			var index = book.likes.indexOf($rootScope.rootLogedUser.username);
			book.likes.splice(index, 1);
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
	
	$scope.passFilter = function(book){
		if($scope.filterType == 0){
			return filterByName(book);
		}
		if($scope.filterType == 1){
			return filterByPrice(book);
		}
		if($scope.filterType == 2){
			return filterByLike(book);
			return true;
		}
	}
	
	var filterByName = function(book){
		if($scope.filterNameValue == ""){
			return true;
		}
		return book.bookName.indexOf($scope.filterNameValue) !== -1;	
	}
	
	var filterByPrice = function(book){
		if($scope.filterMinValue == "" || $scope.filterMaxValue == ""){
			return true;
		}
		return book.price >= $scope.filterMinValue && book.price <=  $scope.filterMaxValue;
	}
	
	var filterByLike = function(book){
		if($scope.filterMinLikeValue == "" || $scope.filterMaxLikeValue == ""){
			return true;
		}
		return book.likes.length >= $scope.filterMinLikeValue && book.likes.length <=  $scope.filterMaxLikeValue;
	}
});
