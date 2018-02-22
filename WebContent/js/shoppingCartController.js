//controller of the shopping cart page

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
