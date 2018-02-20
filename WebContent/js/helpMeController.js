app.controller('helpMeController', function($http, $scope, $rootScope) {
	$scope.submitHelpMessageSucceed = false;
	
	$scope.submitHelpMessage = function(){
		var message = {
				from: $rootScope.rootLogedUser.username,
				to: "Admin",
				content: $scope.helpMessage
		}
		$http.post('messages', message).success(function(data, status, headers, config) {
			$http.get('messages').success(function(data, status, headers, config) {
				$rootScope.rootMessages = data;
				$scope.submitHelpMessageSucceed = true;
			});
		});
	}
});
