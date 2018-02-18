app.controller('userProfileController', function($http, $scope, $rootScope) {
	$scope.user = $rootScope.rootLogedUser;
	$scope.userProfilePath = "HTML/myDetails.html";

	$scope.changeUserProfilePath = function(path) {
		$scope.userProfilePath = path;
	}
});