app.controller('adminMessagesController', function($http, $scope, $rootScope) {
	
	$scope.replyOrReplied = function(message){
		if(message.isViewed == 2){
			return "Replied";
		}
		else{
			return "Reply";
		}
	}
	
	$scope.isForMe = function(message) {
		return message.to == "Admin";
	}

	$scope.getMessageImage = function(message) {
		return "Images/" + message.from + ".jpg";
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
});

app.controller('adminReplyController', function($http, $scope, $rootScope) {
	$scope.replyMessage="";
	
	$scope.submitReplyMessageSucceed = false;
	
	$scope.submitReplyMessage = function(m){
		var message = {
				from: "BooksForAll",
				to: m.from,
				content: $scope.replyMessage,
				
		}
		$http.post('messages', message).success(function(data, status, headers, config) {
			var message = {
					messageId : m.messageId,
					from : m.from,
					to : m.to,
					content : m.content,
					date : m.date,
					isViewed : 2
				}
				$http.put('messages', message).success(
						function(data, status, headers, config) {
							m.isViewed = 2;
							$http.get('messages').success(function(data, status, headers, config) {
								$rootScope.rootMessages = data;
								$scope.submitReplyMessageSucceed = true;
							});
						});
			
		});
	}
});

