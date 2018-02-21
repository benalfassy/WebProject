app.controller('booksController',function($rootScope, $scope, $http) {
	
	$scope.filterNameValue = "";
	
	$scope.filterType = 0;
	
	$scope.filterMyBooks = false;
	
	$scope.filterMinValue = "";
	
	$scope.filterMaxValue = "";
	
	$scope.filterMaxLikeValue = "";
	
	$scope.filterMinLikeValue = "";

	var getResult = $http.get("books");
		
	getResult.success(function(data, status, headers, config) {
		$scope.books = data;
		
		angular.forEach(data,function(book){
			
			$("#"+book.bookName).mouseover(function(){
				$("#"+book.bookName+"-likesContent").show();
			});
			
		    $("#"+book.bookName).mouseout(function(){
		        $("#"+book.bookName+"-likesContent").hide();
		    });
		})
	});
	
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
		return $.inArray($rootScope.rootLogedUser.nickName, book.likes);
	}

	$scope.doLike = function(book) {

		var likeRequest = {
			isLike : true,
			bookName : book.bookName,
			nickName : $rootScope.rootLogedUser.nickName
		}

		var res = $http.put("books", likeRequest);
		res.success(function(data, status, headers, config) {
			book.likes.push($rootScope.rootLogedUser.nickName);
		});
	}

	$scope.doUnLike = function(book) {

		var likeRequest = {
			isLike : false,
			bookName : book.bookName,
			nickName : $rootScope.rootLogedUser.nickName
		}

		var res = $http.put("books", likeRequest);
		
		res.success(function(data, status, headers, config) {
			var index = book.likes.indexOf($rootScope.rootLogedUser.nickName);
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
		if($scope.filterMyBooks == true && $scope.hasBook(book) == -1){
			return false;
		}
		if($scope.filterType == 0){
			return filterByName(book);
		}
		if($scope.filterType == 1){
			return filterByPrice(book);
		}
		if($scope.filterType == 2){
			return filterByLike(book);
		}
		return true;
	}
	
	var filterByName = function(book){
		if($scope.filterNameValue == ""){
			return true;
		}
		return angular.lowercase(book.bookName).indexOf(angular.lowercase($scope.filterNameValue)) !== -1;	
	}
	
	var filterByPrice = function(book){
		
		var min = $scope.filterMinValue;
		var max = $scope.filterMaxValue;
		
		if($scope.filterMinValue == ""){
			min = -Infinity;
		}
		if($scope.filterMaxValue == ""){
			max = Infinity;
		}
		
		return book.price >= min && book.price <=  max;
	}
	
	var filterByLike = function(book){
		
		var min = $scope.filterMinLikeValue;
		var max = $scope.filterMaxLikeValue;
		
		if($scope.filterMinLikeValue == ""){
			min = -Infinity;
		}
		if($scope.filterMaxLikeValue == ""){
			max = Infinity;
		}
		
		return book.likes.length >= min && book.likes.length <=  max;
	}
});
