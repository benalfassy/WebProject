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
			
			var today = new Date
			
			var transaction = {
					username : $rootScope.rootLogedUser.username,
					bookList : createBookList(),
					totalPrice : $scope.amountToPay(),
					date : {
						year : today.getFullYear(),
						month : today.getMonth(),
						day: today.getDate()
					}
			}
			
			$http.post('transactions', transaction);

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
	
	var createBookList = function(){
		var list = [];
		angular.forEach($scope.cartBooks, function(b){
			list.push(b.bookName);
		})
		return list;
	}

});