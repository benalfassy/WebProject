app.controller('userProfileController', function($http, $scope, $rootScope) {

	$scope.user = $rootScope.rootLogedUser;

	$scope.userProfilePath = "HTML/myDetails.html";

	$scope.changeUserProfilePath = function(path) {
		$scope.userProfilePath = path;
	}

	$scope.messagesNavDisplay = function() {

		var newMessages = 0;

		angular.forEach($scope.messages, function(m) {
			if (m.to == $rootScope.rootLogedUser.username && m.isViewed == 0) {
				newMessages++;
			}
		})

		if (newMessages > 0) {
			return "(" + newMessages + ")";
		} else {
			return "";
		}
	}
	
	$scope.deleteMsg = function(message){
		var res = $http.delete('messages/' + message.messageId);
		
		res.success(function(data, status, headers, config) {
			var index = $rootScope.rootMessages.indexOf(message);
			$rootScope.rootMessages.splice(index, 1);
		});

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))

		});
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

	$scope.updateUserStreet = function() {
		$scope.user.street = $scope.street;
		$scope.street = "";
		$scope.toggleStreet = !$scope.toggleStreet;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserCity = function() {
		$scope.user.city = $scope.city;
		$scope.city = "";
		$scope.toggleCity = !$scope.toggleCity;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserZip = function() {
		$scope.user.zipCode = $scope.zipCode;
		$scope.zipCode = "";
		$scope.toggleStreetZip = !$scope.toggleStreetZip;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserStreetNum = function() {
		$scope.user.streetNum = $scope.streetNo;
		$scope.streetNo = "";
		$scope.toggleStreetNum = !$scope.toggleStreetNum;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserPhone = function() {
		$scope.user.phoneNum = $scope.phoneNum;
		$scope.phoneNum = "";
		$scope.togglePhoneNum = !$scope.togglePhoneNum;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserPassword = function() {
		$scope.user.password = $scope.password;
		$scope.password = "";
		$scope.togglePassword = !$scope.togglePassword;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.updateUserDescription = function() {
		$scope.user.description = $scope.description;
		$scope.description = "";
		$scope.toggleDescription = !$scope.toggleDescription;

		var res = $http.put('customers', $scope.user);

		res.error(function(data, status, headers, config) {

			alert("failure message: " + JSON.stringify({
				data : data
			}))
		});
	}

	$scope.getPhoto = function() {
		return "Images/" + $scope.user.username + ".jpg;"
	}

	$scope.isForMe = function(message) {
		return message.to == $rootScope.rootLogedUser.username;
	}

});



