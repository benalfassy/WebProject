//controller for the transaction page of the admin

app.controller('transactionsController', function($rootScope, $scope, $http) {

	$scope.transactions = null;
	var getTransactions = $http.get("transactions");

	//get all the transactions from DB
	getTransactions.success(function(data, status, headers, config) {
		$scope.transactions = data;
	});

	$scope.getDate = function(transaction) {
		var date = transaction.date.year + "-" + transaction.date.month + "-"
				+ transaction.date.day;
		return date;
	}

	$scope.getBookList = function(transaction) {
		var bookList = "";
		angular.forEach(transaction.bookList, function(b) {
			bookList += ("\"" + b + "\", ");
		})
		bookList += "$";
		bookList = bookList.replace(", $", "");
		return bookList;
	}

});
